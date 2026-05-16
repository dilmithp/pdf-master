{{/*
Shared ExternalSecret template (External Secrets Operator).
Iterates over .Values.secrets and creates one ExternalSecret per entry,
mapping the AWS Secrets Manager path into a Kubernetes Secret consumed by the Deployment.
*/}}
{{- define "service.externalsecrets" -}}
{{- range .Values.secrets }}
---
apiVersion: external-secrets.io/v1beta1
kind: ExternalSecret
metadata:
  name: {{ include "service.fullname" $ }}-{{ .name }}-secret
  namespace: {{ $.Release.Namespace }}
  labels:
    {{- include "service.labels" $ | nindent 4 }}
spec:
  refreshInterval: "1h"
  secretStoreRef:
    name: aws-secrets-manager
    kind: ClusterSecretStore
  target:
    name: {{ include "service.fullname" $ }}-{{ .name }}-secret
    creationPolicy: Owner
    deletionPolicy: Retain
  dataFrom:
    - extract:
        key: {{ .remoteRef }}
{{- end }}
{{- end }}
