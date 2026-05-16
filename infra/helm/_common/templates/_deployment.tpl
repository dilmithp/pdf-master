{{/*
Shared Deployment template.
Caller: {{ include "service.deployment" . }}
*/}}
{{- define "service.deployment" -}}
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "service.fullname" . }}
  namespace: {{ .Release.Namespace }}
  labels:
    {{- include "service.labels" . | nindent 4 }}
spec:
  {{- if not .Values.keda.enabled }}
  replicas: {{ .Values.replicaCount }}
  {{- end }}
  selector:
    matchLabels:
      {{- include "service.selectorLabels" . | nindent 6 }}
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 0
      maxSurge: 1
  template:
    metadata:
      labels:
        {{- include "service.selectorLabels" . | nindent 8 }}
      annotations:
        checksum/config: {{ include "service.configmap" . | sha256sum }}
        {{- with .Values.podAnnotations }}
        {{- toYaml . | nindent 8 }}
        {{- end }}
    spec:
      serviceAccountName: {{ include "service.serviceAccountName" . }}
      securityContext:
        {{- toYaml .Values.podSecurityContext | nindent 8 }}
      terminationGracePeriodSeconds: 60
      containers:
        - name: {{ include "service.name" . }}
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          imagePullPolicy: {{ .Values.image.pullPolicy }}
          securityContext:
            {{- toYaml .Values.securityContext | nindent 12 }}
          ports:
            - name: http
              containerPort: {{ .Values.port }}
              protocol: TCP
          envFrom:
            - configMapRef:
                name: {{ include "service.fullname" . }}-config
            {{- range .Values.secrets }}
            - secretRef:
                name: {{ include "service.fullname" $ }}-{{ .name }}-secret
            {{- end }}
          {{- if .Values.env }}
          env:
            {{- range $k, $v := .Values.env }}
            - name: {{ $k }}
              value: {{ $v | quote }}
            {{- end }}
          {{- end }}
          livenessProbe:
            {{- toYaml .Values.livenessProbe | nindent 12 }}
          readinessProbe:
            {{- toYaml .Values.readinessProbe | nindent 12 }}
          resources:
            {{- toYaml .Values.resources | nindent 12 }}
          volumeMounts:
            - name: tmp
              mountPath: /tmp
      volumes:
        - name: tmp
          emptyDir: {}
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
            - weight: 100
              podAffinityTerm:
                topologyKey: kubernetes.io/hostname
                labelSelector:
                  matchLabels:
                    {{- include "service.selectorLabels" . | nindent 20 }}
      topologySpreadConstraints:
        - maxSkew: 1
          topologyKey: topology.kubernetes.io/zone
          whenUnsatisfiable: ScheduleAnyway
          labelSelector:
            matchLabels:
              {{- include "service.selectorLabels" . | nindent 14 }}
{{- end }}
