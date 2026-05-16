variable "services" {
  description = "List of service names for ECR repository creation"
  type        = list(string)
}

variable "account_id" {
  description = "AWS account ID for cross-account pull policy (CI/CD)"
  type        = string
}

variable "environment" {
  type = string
}

variable "image_retention_count" {
  description = "Number of tagged images to retain per repo"
  type        = number
  default     = 30
}

variable "tags" {
  type    = map(string)
  default = {}
}
