# --- IAM Role for ESO (IRSA) ---
data "aws_iam_policy_document" "eso_assume" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRoleWithWebIdentity"]
    principals {
      type        = "Federated"
      identifiers = [var.oidc_provider_arn]
    }
    condition {
      test     = "StringEquals"
      variable = "${var.oidc_provider_url}:sub"
      values   = ["system:serviceaccount:${var.eso_namespace}:external-secrets"]
    }
    condition {
      test     = "StringEquals"
      variable = "${var.oidc_provider_url}:aud"
      values   = ["sts.amazonaws.com"]
    }
  }
}

data "aws_iam_policy_document" "eso_secrets" {
  statement {
    effect = "Allow"
    actions = [
      "secretsmanager:GetSecretValue",
      "secretsmanager:DescribeSecret",
      "secretsmanager:ListSecretVersionIds",
    ]
    resources = length(var.secret_arns) > 0 ? var.secret_arns : ["arn:aws:secretsmanager:${var.aws_region}:${var.account_id}:secret:pdfmaster/*"]
  }
}

resource "aws_iam_role" "eso" {
  name               = "${var.cluster_name}-eso"
  assume_role_policy = data.aws_iam_policy_document.eso_assume.json
  tags               = var.tags
}

resource "aws_iam_policy" "eso_secrets" {
  name   = "${var.cluster_name}-eso-secrets"
  policy = data.aws_iam_policy_document.eso_secrets.json
  tags   = var.tags
}

resource "aws_iam_role_policy_attachment" "eso_secrets" {
  role       = aws_iam_role.eso.name
  policy_arn = aws_iam_policy.eso_secrets.arn
}

# --- Install External Secrets Operator via Helm ---
resource "kubernetes_namespace" "eso" {
  metadata {
    name = var.eso_namespace
    labels = {
      "app.kubernetes.io/managed-by" = "terraform"
    }
  }
}

resource "helm_release" "external_secrets" {
  name       = "external-secrets"
  repository = "https://charts.external-secrets.io"
  chart      = "external-secrets"
  version    = var.eso_chart_version
  namespace  = kubernetes_namespace.eso.metadata[0].name

  set {
    name  = "serviceAccount.annotations.eks\\.amazonaws\\.com/role-arn"
    value = aws_iam_role.eso.arn
  }

  set {
    name  = "installCRDs"
    value = "true"
  }

  depends_on = [aws_iam_role_policy_attachment.eso_secrets]
}

# --- ClusterSecretStore pointing at AWS Secrets Manager ---
resource "kubernetes_manifest" "cluster_secret_store" {
  manifest = {
    apiVersion = "external-secrets.io/v1beta1"
    kind       = "ClusterSecretStore"
    metadata = {
      name = "aws-secrets-manager"
      labels = {
        "app.kubernetes.io/managed-by" = "terraform"
      }
    }
    spec = {
      provider = {
        aws = {
          service = "SecretsManager"
          region  = var.aws_region
          auth = {
            jwt = {
              serviceAccountRef = {
                name      = "external-secrets"
                namespace = var.eso_namespace
              }
            }
          }
        }
      }
    }
  }

  depends_on = [helm_release.external_secrets]
}
