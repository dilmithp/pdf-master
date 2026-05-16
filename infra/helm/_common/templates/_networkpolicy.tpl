{{/*
Shared NetworkPolicy template.
Default-deny ingress and egress; opens only explicitly allowed ports.

VPC endpoint CIDRs (us-east-1 defaults — override via networkPolicy.s3VpcEndpointCidr etc.):
  S3 gateway endpoint: routed via VPC endpoint, allow 443 to vpc endpoint prefix list.
  We emit CIDR-based egress rules here; the actual prefix-list is wired in the VPC module.
*/}}
{{- define "service.networkpolicy" -}}
{{- if .Values.networkPolicy.enabled -}}
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: {{ include "service.fullname" . }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
spec:
  podSelector:
    matchLabels:
      {{- include "service.selectorLabels" . | nindent 6 }}
  policyTypes:
    - Ingress
    - Egress

  ingress:
    # Allow traffic from the gateway (or any pod in the namespace with the gateway label)
    - from:
        - namespaceSelector:
            matchLabels:
              kubernetes.io/metadata.name: pdfmaster
          podSelector:
            matchLabels:
              app.kubernetes.io/name: gateway
      ports:
        - port: {{ .Values.port }}
          protocol: TCP
    # Allow Prometheus scraping from monitoring namespace
    - from:
        - namespaceSelector:
            matchLabels:
              kubernetes.io/metadata.name: monitoring
      ports:
        - port: {{ .Values.port }}
          protocol: TCP

  egress:
    {{- if .Values.networkPolicy.egress.allowDNS }}
    # DNS resolution via kube-dns
    - to:
        - namespaceSelector:
            matchLabels:
              kubernetes.io/metadata.name: kube-system
          podSelector:
            matchLabels:
              k8s-app: kube-dns
      ports:
        - port: 53
          protocol: UDP
        - port: 53
          protocol: TCP
    {{- end }}
    {{- if .Values.networkPolicy.egress.allowS3 }}
    # AWS S3 via VPC gateway endpoint (HTTPS only)
    - ports:
        - port: 443
          protocol: TCP
      to:
        - ipBlock:
            cidr: 0.0.0.0/0
            except:
              - 10.0.0.0/8
              - 172.16.0.0/12
              - 192.168.0.0/16
    {{- end }}
    {{- if .Values.networkPolicy.egress.allowRDS }}
    # PostgreSQL RDS within the namespace / VPC
    - to:
        - namespaceSelector:
            matchLabels:
              kubernetes.io/metadata.name: pdfmaster
      ports:
        - port: 5432
          protocol: TCP
    {{- end }}
    {{- if .Values.networkPolicy.egress.allowRedis }}
    # ElastiCache Redis within the VPC
    - ports:
        - port: 6379
          protocol: TCP
      to:
        - ipBlock:
            cidr: 10.0.0.0/8
    {{- end }}
    {{- if .Values.networkPolicy.egress.allowRabbitMQ }}
    # Amazon MQ RabbitMQ AMQP within the VPC
    - ports:
        - port: 5671
          protocol: TCP
        - port: 443
          protocol: TCP
      to:
        - ipBlock:
            cidr: 10.0.0.0/8
    {{- end }}
    {{- if .Values.networkPolicy.egress.allowGateway }}
    # Allow outbound to gateway (for service-to-service via gateway)
    - to:
        - namespaceSelector:
            matchLabels:
              kubernetes.io/metadata.name: pdfmaster
          podSelector:
            matchLabels:
              app.kubernetes.io/name: gateway
      ports:
        - port: 8080
          protocol: TCP
    {{- end }}
    {{- range .Values.networkPolicy.egress.extraCidrs }}
    - to:
        - ipBlock:
            cidr: {{ .cidr }}
      ports:
        - port: {{ .port }}
          protocol: {{ .protocol | default "TCP" }}
    {{- end }}
{{- end }}
{{- end }}
