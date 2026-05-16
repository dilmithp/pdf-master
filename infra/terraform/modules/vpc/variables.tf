variable "cluster_name" {
  description = "EKS cluster name — used to tag subnets for AWS LB Controller discovery"
  type        = string
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "List of AZs (must be 3 for Multi-AZ coverage)"
  type        = list(string)
}

variable "environment" {
  description = "Deployment environment label"
  type        = string
}

variable "aws_region" {
  description = "AWS region (used for S3 VPC endpoint service name)"
  type        = string
  default     = "us-east-1"
}

variable "tags" {
  description = "Common resource tags"
  type        = map(string)
  default     = {}
}
