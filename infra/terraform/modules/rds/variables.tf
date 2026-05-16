variable "cluster_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "subnet_ids" {
  description = "DB subnet IDs"
  type        = list(string)
}

variable "allowed_security_group_ids" {
  description = "Security groups that may connect to RDS (EKS node SG)"
  type        = list(string)
  default     = []
}

variable "instance_class" {
  type    = string
  default = "db.r6g.large"
}

variable "engine_version" {
  type    = string
  default = "16.4"
}

variable "allocated_storage" {
  type    = number
  default = 100
}

variable "max_allocated_storage" {
  type    = number
  default = 1000
}

variable "multi_az" {
  type    = bool
  default = true
}

variable "backup_retention_days" {
  type    = number
  default = 14
}

variable "master_password_secret_arn" {
  description = "ARN of the Secrets Manager secret that holds the RDS master password"
  type        = string
}

variable "tags" {
  type    = map(string)
  default = {}
}
