variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
}

variable "kubernetes_version" {
  description = "Kubernetes version"
  type        = string
  default     = "1.31"
}

variable "vpc_id" {
  description = "VPC ID where the cluster lives"
  type        = string
}

variable "subnet_ids" {
  description = "Private subnet IDs for the EKS control plane and node group"
  type        = list(string)
}

variable "node_instance_type" {
  description = "EC2 instance type for managed node group"
  type        = string
  default     = "m6g.large"
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

variable "environment" {
  type = string
}

variable "tags" {
  type    = map(string)
  default = {}
}
