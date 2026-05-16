output "vpc_id" {
  description = "VPC ID"
  value       = module.vpc.vpc_id
}

output "private_subnet_ids" {
  description = "Private subnet IDs (EKS nodes and RDS reside here)"
  value       = module.vpc.private_subnet_ids
}

output "public_subnet_ids" {
  description = "Public subnet IDs (load balancer subnets)"
  value       = module.vpc.public_subnet_ids
}

output "cluster_name" {
  description = "EKS cluster name"
  value       = module.eks.cluster_name
}

output "cluster_endpoint" {
  description = "EKS API server endpoint"
  value       = module.eks.cluster_endpoint
  sensitive   = true
}

output "cluster_certificate_authority_data" {
  description = "Base64-encoded cluster CA certificate"
  value       = module.eks.cluster_certificate_authority_data
  sensitive   = true
}

output "oidc_provider_arn" {
  description = "IAM OIDC provider ARN for IRSA (IAM Roles for Service Accounts)"
  value       = module.eks.oidc_provider_arn
}

output "oidc_provider_url" {
  description = "IAM OIDC provider URL (without https://)"
  value       = module.eks.oidc_provider_url
}

output "rds_endpoint" {
  description = "RDS PostgreSQL cluster endpoint"
  value       = module.rds.endpoint
  sensitive   = true
}

output "rds_reader_endpoint" {
  description = "RDS PostgreSQL reader endpoint"
  value       = module.rds.reader_endpoint
  sensitive   = true
}

output "elasticache_primary_endpoint" {
  description = "ElastiCache Redis primary endpoint"
  value       = module.elasticache.primary_endpoint
  sensitive   = true
}

output "mq_broker_endpoint" {
  description = "Amazon MQ RabbitMQ AMQP endpoint"
  value       = module.amazon_mq.broker_endpoint
  sensitive   = true
}

output "ecr_repository_urls" {
  description = "Map of service name to ECR repository URL"
  value       = module.ecr.repository_urls
}

output "kubeconfig_command" {
  description = "Command to update local kubeconfig"
  value       = "aws eks update-kubeconfig --region ${var.aws_region} --name ${module.eks.cluster_name}"
}
