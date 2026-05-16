{{/*
Shared ServiceAccount template.
*/}}
{{- define "service.serviceaccount" -}}
{{- if .Values.serviceAccount.create -}}
apiVersion: v1
kind: ServiceAccount
metadata:
  name: {{ include "service.serviceAccountName" . }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
  annotations:
    {{- toYaml .Values.serviceAccount.annotations | nindent 4 }}
automountServiceAccountToken: false
{{- end }}
{{- end }}
