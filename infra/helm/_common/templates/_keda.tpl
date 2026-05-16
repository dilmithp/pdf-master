{{/*
Shared KEDA ScaledObject template for RabbitMQ-driven queue workers.
*/}}
{{- define "service.keda" -}}
{{- if .Values.keda.enabled -}}
apiVersion: keda.sh/v1alpha1
kind: ScaledObject
metadata:
  name: {{ include "service.fullname" . }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: {{ include "service.fullname" . }}
  pollingInterval: 15
  cooldownPeriod: 60
  minReplicaCount: {{ .Values.keda.minReplicas | default 0 }}
  maxReplicaCount: {{ .Values.keda.maxReplicas | default 20 }}
  triggers:
    - type: rabbitmq
      metadata:
        protocol: amqp
        queueName: {{ .Values.keda.queueName }}
        mode: QueueLength
        value: {{ .Values.keda.targetMessageCount | default 10 | quote }}
      authenticationRef:
        name: rabbitmq-trigger-auth
{{- end }}
{{- end }}
