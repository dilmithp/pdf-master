{{/*
Shared ConfigMap template for non-secret environment configuration.
Services override .Values.config with their own key-value pairs.
*/}}
{{- define "service.configmap" -}}
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ include "service.fullname" . }}-config
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
data:
  SPRING_PROFILES_ACTIVE: "prod"
  SERVER_PORT: {{ .Values.port | quote }}
  MANAGEMENT_SERVER_PORT: {{ .Values.port | quote }}
  MANAGEMENT_ENDPOINTS_WEB_BASE_PATH: "/actuator"
  OTEL_SERVICE_NAME: {{ include "service.fullname" . }}
  OTEL_EXPORTER_OTLP_ENDPOINT: "http://otel-collector.monitoring.svc.cluster.local:4317"
  {{- range $k, $v := .Values.config }}
  {{ $k }}: {{ $v | quote }}
  {{- end }}
{{- end }}
