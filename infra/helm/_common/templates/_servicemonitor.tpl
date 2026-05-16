{{/*
Shared ServiceMonitor template for Prometheus Operator.
*/}}
{{- define "service.servicemonitor" -}}
{{- if .Values.serviceMonitor.enabled -}}
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: {{ include "service.fullname" . }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
    release: prometheus  # matches kube-prometheus-stack serviceMonitorSelector
spec:
  selector:
    matchLabels:
      {{- include "service.selectorLabels" . | nindent 6 }}
  namespaceSelector:
    matchNames:
      - {{ .Release.Namespace }}
  endpoints:
    - port: http
      path: {{ .Values.serviceMonitor.path }}
      interval: {{ .Values.serviceMonitor.interval }}
      scheme: http
      honorLabels: false
{{- end }}
{{- end }}
