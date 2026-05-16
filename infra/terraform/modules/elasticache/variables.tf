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
  type = list(string)
}

variable "allowed_security_group_ids" {
  type    = list(string)
  default = []
}

variable "node_type" {
  type    = string
  default = "cache.t4g.medium"
}

variable "num_cache_nodes" {
  type    = number
  default = 2
}

variable "redis_version" {
  type    = string
  default = "7.1"
}

variable "auth_token_secret_arn" {
  description = "Secrets Manager ARN containing the Redis auth token"
  type        = string
}

variable "tags" {
  type    = map(string)
  default = {}
}
