variable "environment" {
  type = string
}

variable "kms_key_arn" {
  description = "KMS key ARN for Secrets Manager encryption"
  type        = string
  default     = ""  # empty = use AWS managed key
}

variable "tags" {
  type    = map(string)
  default = {}
}
