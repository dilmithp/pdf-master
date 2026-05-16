output "vpc_id" {
  value = module.vpc.vpc_id
}

output "cluster_name" {
  value = module.eks.cluster_name
}

output "cluster_endpoint" {
  value     = module.eks.cluster_endpoint
  sensitive = true
}

output "oidc_provider_arn" {
  value = module.eks.oidc_provider_arn
}

output "rds_endpoint" {
  value     = module.rds.endpoint
  sensitive = true
}

output "elasticache_primary_endpoint" {
  value     = module.elasticache.primary_endpoint
  sensitive = true
}

output "mq_broker_endpoint" {
  value     = module.amazon_mq.broker_endpoint
  sensitive = true
}

output "ecr_repository_urls" {
  value = module.ecr.repository_urls
}

output "kubeconfig_command" {
  value = "aws eks update-kubeconfig --region ${var.aws_region} --name ${module.eks.cluster_name}"
}
