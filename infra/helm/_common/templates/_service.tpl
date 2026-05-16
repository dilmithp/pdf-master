{{/*
Shared Service template.
*/}}
{{- define "service.service" -}}
apiVersion: v1
kind: Service
metadata:
  name: {{ include "service.fullname" . }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
spec:
  type: {{ .Values.service.type }}
  selector:
    {{- include "service.selectorLabels" . | nindent 4 }}
  ports:
    - name: http
      port: {{ .Values.port }}
      targetPort: http
      protocol: TCP
{{- end }}
