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
  description = "Subnet IDs for broker (1 for single-instance, 2 for active/standby)"
  type        = list(string)
}

variable "allowed_security_group_ids" {
  type    = list(string)
  default = []
}

variable "instance_type" {
  type    = string
  default = "mq.m5.large"
}

variable "engine_version" {
  type    = string
  default = "3.13"
}

variable "deployment_mode" {
  description = "SINGLE_INSTANCE or ACTIVE_STANDBY_MULTI_AZ"
  type        = string
  default     = "ACTIVE_STANDBY_MULTI_AZ"
}

variable "connection_secret_arn" {
  description = "Secrets Manager ARN with RabbitMQ admin credentials"
  type        = string
}

variable "tags" {
  type    = map(string)
  default = {}
}
