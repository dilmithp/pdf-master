{{- include "service.deployment" . }}
---
{{- include "service.service" . }}
---
{{- include "service.serviceaccount" . }}
---
{{- include "service.configmap" . }}
---
{{- include "service.hpa" . }}
---
{{- include "service.pdb" . }}
---
{{- include "service.networkpolicy" . }}
---
{{- include "service.servicemonitor" . }}
---
{{- include "service.externalsecrets" . }}
