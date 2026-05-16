# PDF-Master Observability Stack

This directory contains Helm values and configuration for the PDF-Master observability stack.

## Components

| Component | Chart | Purpose |
|-----------|-------|---------|
| OTel Collector | `open-telemetry/opentelemetry-collector` | Unified telemetry pipeline |
| kube-prometheus-stack | `prometheus-community/kube-prometheus-stack` | Self-hosted Prometheus + Alertmanager (optional) |
| Grafana Dashboards | JSON exports | RED metrics, queue depth, auth, billing, CWV |
| Prometheus Rules | PrometheusRule CRDs | SLO burn-rate, infra, and business alerts |

## Quick Start — Grafana Cloud (recommended)

Requires the following environment variables / Kubernetes Secrets:

```
GRAFANA_CLOUD_METRICS_URL   # e.g. https://prometheus-prod-24-prod-eu-west-2.grafana.net/api/prom/push
GRAFANA_CLOUD_LOGS_URL      # e.g. https://logs-prod-eu-west-0.grafana.net/loki/api/v1/push
GRAFANA_CLOUD_TRACES_URL    # e.g. tempo-eu-west-0.grafana.net:443
GRAFANA_CLOUD_INSTANCE_ID   # numeric instance ID from Grafana Cloud portal
GRAFANA_CLOUD_API_KEY       # Grafana Cloud API key with MetricsPublisher + LogsPublisher + TracesPublisher scopes
```

### Install OTel Collector

```bash
helm repo add open-telemetry https://open-telemetry.github.io/opentelemetry-helm-charts
helm repo update

kubectl create namespace monitoring

kubectl create secret generic otel-collector-secrets \
  --namespace monitoring \
  --from-literal=GRAFANA_CLOUD_METRICS_URL="${GRAFANA_CLOUD_METRICS_URL}" \
  --from-literal=GRAFANA_CLOUD_LOGS_URL="${GRAFANA_CLOUD_LOGS_URL}" \
  --from-literal=GRAFANA_CLOUD_TRACES_URL="${GRAFANA_CLOUD_TRACES_URL}" \
  --from-literal=GRAFANA_CLOUD_INSTANCE_ID="${GRAFANA_CLOUD_INSTANCE_ID}" \
  --from-literal=GRAFANA_CLOUD_API_KEY="${GRAFANA_CLOUD_API_KEY}"

helm upgrade --install otel-collector open-telemetry/opentelemetry-collector \
  --namespace monitoring \
  --values otel-collector/values.yaml \
  --set envFrom[0].secretRef.name=otel-collector-secrets
```

### Apply Prometheus Alert Rules

```bash
kubectl apply -f prometheus/rules/slo.yaml
kubectl apply -f prometheus/rules/infrastructure.yaml
kubectl apply -f prometheus/rules/business.yaml
```

### Import Grafana Dashboards

Dashboards are in `grafana/dashboards/`. Import via:

1. Grafana UI: Dashboards > Import > Upload JSON file
2. Or apply as ConfigMaps with the `grafana_dashboard: "1"` label if using the Grafana sidecar:

```bash
for f in grafana/dashboards/*.json; do
  name=$(basename "$f" .json)
  kubectl create configmap "grafana-dashboard-${name}" \
    --namespace monitoring \
    --from-file="${name}.json=${f}" \
    --dry-run=client -o yaml | \
    kubectl label --local -f - grafana_dashboard=1 -o yaml | \
    kubectl apply -f -
done
```

## Quick Start — Self-Hosted Prometheus (fallback)

Use this if Grafana Cloud is not an option.

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

kubectl create secret generic prometheus-secrets \
  --namespace monitoring \
  --from-literal=GRAFANA_ADMIN_PASSWORD="${GRAFANA_ADMIN_PASSWORD}" \
  --from-literal=PAGERDUTY_INTEGRATION_KEY="${PAGERDUTY_INTEGRATION_KEY}" \
  --from-literal=SLACK_WEBHOOK_URL="${SLACK_WEBHOOK_URL}"

helm upgrade --install kube-prometheus-stack prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace \
  --values prometheus/values.yaml \
  --set alertmanager.config.receivers[0].pagerduty_configs[0].service_key="${PAGERDUTY_INTEGRATION_KEY}" \
  --set grafana.adminPassword="${GRAFANA_ADMIN_PASSWORD}"
```

## Alert Routing

| Severity | Receiver | Response |
|----------|----------|----------|
| `critical` | PagerDuty | Page on-call engineer immediately |
| `warning` | Slack `#alerts-warnings` | Review within business hours |

### 5 Critical Paging Alerts

1. **PDFMasterHighErrorRateCritical** — HTTP error rate > 1% for 5m
2. **PDFMasterHighLatencyCritical** — p95 latency > 1s for 5m
3. **PDFMasterRabbitMQQueueDepthCritical** — any queue > 1000 messages for 10m
4. **PDFMasterDBConnectionSaturationCritical** — connection pool > 85% for 3m
5. **PDFMasterRDSLowFreeStorageCritical** — RDS free disk < 15 GB for 5m

## Service Instrumentation Requirements

Spring Boot services must expose metrics at `/actuator/prometheus` and carry the following pod annotations:

```yaml
annotations:
  prometheus.io/scrape: "true"
  prometheus.io/port: "8080"
  prometheus.io/path: "/actuator/prometheus"
```

Next.js frontend sends Web Vitals via Sentry SDK; CWV metrics arrive as OTel metrics through the collector.
