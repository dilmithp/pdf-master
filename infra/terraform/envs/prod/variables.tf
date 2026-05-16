variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "account_id" {
  type = string
}

variable "environment" {
  type    = string
  default = "prod"
}

variable "cluster_name" {
  type    = string
  default = "pdfmaster-prod"
}

variable "kubernetes_version" {
  type    = string
  default = "1.31"
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "availability_zones" {
  type    = list(string)
  default = ["us-east-1a", "us-east-1b", "us-east-1c"]
}

variable "node_instance_type" {
  type    = string
  default = "m6g.large"
}

variable "node_min_size" {
  type    = number
  default = 3
}

variable "node_max_size" {
  type    = number
  default = 20
}

variable "node_desired_size" {
  type    = number
  default = 3
}

variable "rds_instance_class" {
  type    = string
  default = "db.r6g.large"
}

variable "rds_allocated_storage" {
  type    = number
  default = 100
}

variable "rds_max_allocated_storage" {
  type    = number
  default = 1000
}

variable "rds_postgres_version" {
  type    = string
  default = "16.4"
}

variable "elasticache_node_type" {
  type    = string
  default = "cache.t4g.medium"
}

variable "elasticache_num_cache_nodes" {
  type    = number
  default = 2
}

variable "mq_instance_type" {
  type    = string
  default = "mq.m5.large"
}

variable "mq_rabbitmq_version" {
  type    = string
  default = "3.13"
}

variable "services" {
  type = list(string)
  default = [
    "gateway",
    "auth-service",
    "billing-service",
    "esign-service",
    "notification-service",
    "pdf-core-service",
    "pdf-ai-service",
    "pdf-convert-service",
    "pdf-ocr-service",
  ]
}

variable "tags" {
  type = map(string)
  default = {
    Project     = "pdfmaster"
    Environment = "prod"
    ManagedBy   = "terraform"
  }
}
