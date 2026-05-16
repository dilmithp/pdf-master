variable "cluster_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "oidc_provider_arn" {
  description = "IAM OIDC provider ARN for IRSA"
  type        = string
}

variable "oidc_provider_url" {
  description = "IAM OIDC provider URL without https://"
  type        = string
}

variable "account_id" {
  type = string
}

variable "aws_region" {
  type = string
}

variable "secret_arns" {
  description = "List of Secrets Manager ARNs that ESO needs read access to"
  type        = list(string)
  default     = []
}

variable "eso_namespace" {
  description = "Kubernetes namespace for the ESO installation"
  type        = string
  default     = "external-secrets"
}

variable "eso_chart_version" {
  description = "Helm chart version for external-secrets"
  type        = string
  default     = "0.10.7"
}

variable "tags" {
  type    = map(string)
  default = {}
}
