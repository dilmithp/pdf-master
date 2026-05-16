# pdfmaster Infrastructure

This directory contains all production infrastructure-as-code for pdfmaster.

## Architecture

```
                        +--------------------------+
  Internet/Cloudflare   |   AWS us-east-1          |
  WAF --> NLB (public)  |                          |
          |             |  EKS cluster             |
          v             |  pdfmaster-prod          |
    [ gateway pod ]     |  (3 AZ, m6g.large)       |
          |             |                          |
    ------+------       |  pdfmaster namespace      |
    |     |     |       |  9 Spring Boot services  |
    v     v     v       |                          |
  auth billing esign    |  Keda ScaledObjects       |
                        |  for 4 PDF worker svcs    |
  pdf-core              |  (min 0, max 20 replicas)|
  pdf-ai         <------+  RabbitMQ triggers        |
  pdf-convert           |                          |
  pdf-ocr               |  ExternalSecrets ESO      |
  notification          |  <- Secrets Manager       |
                        |                          |
                        |  RDS Postgres 16          |
                        |  (Multi-AZ, r6g.large)   |
                        |                          |
                        |  ElastiCache Redis 7      |
                        |  (t4g.medium x2)         |
                        |                          |
                        |  Amazon MQ RabbitMQ       |
                        |  (Active/Standby Multi-AZ)|
                        |                          |
                        |  S3 (via VPC endpoint)   |
                        +--------------------------+
```

## Prerequisites

| Tool        | Minimum version |
|-------------|-----------------|
| Terraform   | 1.9.0           |
| kubectl     | 1.31            |
| helm        | 3.16            |
| awscli      | 2.x             |
| argocd CLI  | 2.13            |

## Directory layout

```
infra/
├── helm/
│   ├── _common/          # Library chart — shared templates for all services
│   └── <service>/        # 9 thin service charts (mostly values.yaml)
├── argocd/
│   ├── applicationset.yaml    # Auto-discovers all 9 services
│   ├── app-project.yaml       # ArgoCD AppProject with RBAC
│   └── bootstrap/
│       ├── argocd-install.yaml  # Installs ArgoCD itself
│       └── app-of-apps.yaml     # Root App-of-Apps
├── terraform/
│   ├── modules/
│   │   ├── vpc/              # VPC, 3 AZ, subnets, NAT, S3 endpoint
│   │   ├── eks/              # EKS cluster, OIDC, managed node group
│   │   ├── rds/              # RDS Postgres 16 Multi-AZ
│   │   ├── elasticache/      # ElastiCache Redis 7
│   │   ├── amazon-mq/        # Amazon MQ RabbitMQ
│   │   ├── ecr/              # ECR repos x9 with lifecycle policies
│   │   ├── secrets/          # Secrets Manager entries (placeholder values)
│   │   └── external-secrets/ # ESO install + ClusterSecretStore
│   └── envs/
│       ├── prod/             # Production configuration
│       └── staging/          # Staging configuration (smaller instances)
└── kubernetes/               # One-off manifests (namespace, NetworkPolicy baseline, KEDA auth)
```

## Bootstrap order

**Step 1 — Create Terraform state backend (once)**

```bash
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

aws s3api create-bucket \
  --bucket pdfmaster-tf-state-${ACCOUNT_ID} \
  --region us-east-1

aws s3api put-bucket-versioning \
  --bucket pdfmaster-tf-state-${ACCOUNT_ID} \
  --versioning-configuration Status=Enabled

aws s3api put-bucket-encryption \
  --bucket pdfmaster-tf-state-${ACCOUNT_ID} \
  --server-side-encryption-configuration \
  '{"Rules":[{"ApplyServerSideEncryptionByDefault":{"SSEAlgorithm":"AES256"}}]}'

aws dynamodb create-table \
  --table-name pdfmaster-tf-locks \
  --attribute-definitions AttributeName=LockID,AttributeType=S \
  --key-schema AttributeName=LockID,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --region us-east-1
```

**Step 2 — Apply Terraform**

```bash
cd infra/terraform/envs/prod

# Copy and edit the example vars file
cp terraform.tfvars.example terraform.tfvars
# Edit terraform.tfvars: set account_id and any overrides

# Init with backend config
terraform init -backend-config=backend.hcl

# Plan and apply (creates VPC, EKS, RDS, ElastiCache, MQ, ECR, Secrets)
terraform plan -out=tfplan
terraform apply tfplan
```

**Step 3 — Update kubeconfig**

```bash
aws eks update-kubeconfig --region us-east-1 --name pdfmaster-prod
```

**Step 4 — Apply baseline Kubernetes manifests**

```bash
kubectl apply -f infra/kubernetes/namespace.yaml
kubectl apply -f infra/kubernetes/network-policies.yaml
kubectl apply -f infra/kubernetes/external-secrets-storeauth.yaml
```

**Step 5 — Install ArgoCD**

```bash
kubectl create namespace argocd

# Bootstrap ArgoCD via its own Application manifest
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/v2.13.3/manifests/install.yaml

# Wait for ArgoCD to be ready
kubectl rollout status deployment argocd-server -n argocd --timeout=300s
```

**Step 6 — Bootstrap App-of-Apps**

```bash
# Update the repoURL in infra/argocd/app-project.yaml and applicationset.yaml
# to point at your actual Git repository before applying.

kubectl apply -f infra/argocd/app-project.yaml
kubectl apply -f infra/argocd/applicationset.yaml
```

From here, ArgoCD auto-discovers all services from `infra/helm/<service>/` and syncs them.

**Step 7 — Apply KEDA TriggerAuthentication**

After ESO has synced the `rabbitmq-connection` secret:

```bash
kubectl apply -f infra/kubernetes/rabbitmq-trigger-auth.yaml
```

## Per-environment workflow

### Deploy a new image tag

ArgoCD does not auto-update image tags. After CI pushes a new image to ECR, update the tag via:

```bash
# Option A: ArgoCD CLI (recommended for production)
argocd app set pdfmaster-pdf-core-service \
  --helm-set image.tag=sha-abc1234 \
  -n argocd

# Option B: Update values-prod.yaml in infra/helm/<service>/ and push to main
# ArgoCD will auto-sync within the configured polling interval (default 3 min)
```

### Rollback

```bash
argocd app rollback pdfmaster-<service> <revision-number>
```

## Secrets rotation runbook

All secrets are stored in AWS Secrets Manager under `pdfmaster/<env>/<service>/<key>`.

1. Navigate to Secrets Manager in the AWS Console.
2. Select the secret to rotate.
3. Click "Retrieve secret value" > "Edit" to update the value.
4. ESO refreshes Kubernetes Secrets on its configured `refreshInterval` (1 hour).
5. For immediate refresh: `kubectl annotate externalsecret <name> -n pdfmaster force-sync=$(date +%s) --overwrite`
6. Restart the affected deployment: `kubectl rollout restart deployment/<svc> -n pdfmaster`

For database credentials, coordinate with a maintenance window and update both the
Secrets Manager value and the RDS instance password.

## Cost estimate (us-east-1, prod, minimum load)

| Resource                              | Monthly estimate  |
|---------------------------------------|-------------------|
| EKS control plane                     | $73               |
| EKS nodes: 3x m6g.large ($0.077/h)   | ~$169             |
| RDS r6g.large Multi-AZ               | ~$250             |
| ElastiCache t4g.medium x2            | ~$60              |
| Amazon MQ m5.large Active/Standby    | ~$140             |
| NAT Gateway (3x, data transfer)       | ~$100             |
| ECR storage + data transfer           | ~$20              |
| Secrets Manager (10 secrets)          | ~$4               |
| CloudWatch Logs                       | ~$30              |
| **Estimated total**                   | **~$850-1,200/mo**|

Costs scale with traffic (NAT, data transfer) and KEDA worker scale-out (additional nodes).

## Production hardening TODO

The following items are not yet implemented and are required before handling production traffic:

- [ ] Custom domain + ACM certificate + Ingress NGINX or AWS ALB Ingress Controller
- [ ] Cloudflare WAF rules (OWASP ruleset, rate limiting, bot management)
- [ ] AWS Cost Anomaly Detection + billing alarms (SNS -> PagerDuty)
- [ ] VPC Flow Logs to S3 with Athena queries
- [ ] AWS GuardDuty enabled on the account
- [ ] AWS Backup vault for RDS automated snapshots (cross-region copy)
- [ ] RDS Proxy for connection pooling (high-concurrency services)
- [ ] EKS Pod Security Standards (enforce restricted PSS)
- [ ] Falco or similar runtime security monitoring
- [ ] Restrict EKS API public access to VPN CIDR (`public_access_cidrs`)
- [ ] Separate ECR pull-through cache for third-party base images
- [ ] ArgoCD SSO via Okta/Google (replace admin password)
- [ ] Cluster Autoscaler or Karpenter for node provisioning
- [ ] eu-west-1 DR replica (GDPR requirement for EU customers)
- [ ] S3 bucket for documents (versioning, Object Lock, replication)
- [ ] OpenTelemetry Collector + Grafana Cloud integration
- [ ] Prometheus AlertManager rules for SLO breach notifications
