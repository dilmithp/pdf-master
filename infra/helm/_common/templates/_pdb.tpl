{{/*
Shared PodDisruptionBudget template.
Workers managed by KEDA use maxUnavailable: 0 during scale-down;
stateless HTTP services use minAvailable: 1.
*/}}
{{- define "service.pdb" -}}
{{- if .Values.podDisruptionBudget.enabled -}}
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: {{ include "service.fullname" . }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
spec:
  selector:
    matchLabels:
      {{- include "service.selectorLabels" . | nindent 6 }}
  {{- if .Values.keda.enabled }}
  maxUnavailable: 0
  {{- else }}
  minAvailable: {{ .Values.podDisruptionBudget.minAvailable }}
  {{- end }}
{{- end }}
{{- end }}
