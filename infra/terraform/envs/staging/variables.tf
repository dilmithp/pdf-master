variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "account_id" {
  type = string
}

variable "environment" {
  type    = string
  default = "staging"
}

variable "cluster_name" {
  type    = string
  default = "pdfmaster-staging"
}

variable "kubernetes_version" {
  type    = string
  default = "1.31"
}

variable "vpc_cidr" {
  type    = string
  default = "10.1.0.0/16"
}

variable "availability_zones" {
  type    = list(string)
  default = ["us-east-1a", "us-east-1b", "us-east-1c"]
}

variable "node_instance_type" {
  type    = string
  default = "t4g.medium"  # smaller for staging
}

variable "node_min_size" {
  type    = number
  default = 2
}

variable "node_max_size" {
  type    = number
  default = 6
}

variable "node_desired_size" {
  type    = number
  default = 2
}

variable "rds_instance_class" {
  type    = string
  default = "db.t4g.medium"  # smaller for staging
}

variable "rds_allocated_storage" {
  type    = number
  default = 20
}

variable "rds_max_allocated_storage" {
  type    = number
  default = 100
}

variable "rds_postgres_version" {
  type    = string
  default = "16.4"
}

variable "elasticache_node_type" {
  type    = string
  default = "cache.t4g.small"
}

variable "elasticache_num_cache_nodes" {
  type    = number
  default = 1
}

variable "mq_instance_type" {
  type    = string
  default = "mq.t3.micro"
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
    Environment = "staging"
    ManagedBy   = "terraform"
  }
}
