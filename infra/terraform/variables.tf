variable "aws_region" {
  description = "Primary AWS region"
  type        = string
  default     = "us-east-1"
}

# eu-west-1 is the planned GDPR expansion region — uncomment when ready
# variable "aws_region_eu" {
#   description = "EU AWS region (GDPR)"
#   type        = string
#   default     = "eu-west-1"
# }

variable "account_id" {
  description = "AWS account ID (used for ECR image paths, IAM ARNs, etc.)"
  type        = string
  # No default — must be supplied via tfvars or environment
}

variable "environment" {
  description = "Deployment environment (prod | staging)"
  type        = string
  default     = "prod"

  validation {
    condition     = contains(["prod", "staging"], var.environment)
    error_message = "environment must be 'prod' or 'staging'."
  }
}

variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
  default     = "pdfmaster-prod"
}

variable "kubernetes_version" {
  description = "EKS Kubernetes version"
  type        = string
  default     = "1.31"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "List of AZs to use (3 required for Multi-AZ RDS)"
  type        = list(string)
  default     = ["us-east-1a", "us-east-1b", "us-east-1c"]
}

variable "node_instance_type" {
  description = "EC2 instance type for EKS managed node group"
  type        = string
  default     = "m6g.large"
}

variable "node_min_size" {
  description = "Minimum nodes in default node group"
  type        = number
  default     = 3
}

variable "node_max_size" {
  description = "Maximum nodes in default node group"
  type        = number
  default     = 20
}

variable "node_desired_size" {
  description = "Desired nodes in default node group"
  type        = number
  default     = 3
}

variable "rds_instance_class" {
  description = "RDS instance class"
  type        = string
  default     = "db.r6g.large"
}

variable "rds_allocated_storage" {
  description = "Initial RDS allocated storage in GB"
  type        = number
  default     = 100
}

variable "rds_max_allocated_storage" {
  description = "Maximum RDS autoscaling storage in GB"
  type        = number
  default     = 1000
}

variable "rds_postgres_version" {
  description = "PostgreSQL engine version"
  type        = string
  default     = "16.4"
}

variable "elasticache_node_type" {
  description = "ElastiCache node type"
  type        = string
  default     = "cache.t4g.medium"
}

variable "elasticache_num_cache_nodes" {
  description = "Number of ElastiCache nodes"
  type        = number
  default     = 2
}

variable "mq_instance_type" {
  description = "Amazon MQ broker instance type"
  type        = string
  default     = "mq.m5.large"
}

variable "mq_rabbitmq_version" {
  description = "RabbitMQ engine version on Amazon MQ"
  type        = string
  default     = "3.13"
}

variable "services" {
  description = "List of service names for ECR repos and IAM roles"
  type        = list(string)
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
  description = "Common resource tags applied to all resources"
  type        = map(string)
  default = {
    Project     = "pdfmaster"
    ManagedBy   = "terraform"
    Environment = "prod"
  }
}
