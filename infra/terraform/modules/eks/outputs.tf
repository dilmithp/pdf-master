output "cluster_name" {
  value = aws_eks_cluster.main.name
}

output "cluster_endpoint" {
  value     = aws_eks_cluster.main.endpoint
  sensitive = true
}

output "cluster_certificate_authority_data" {
  value     = aws_eks_cluster.main.certificate_authority[0].data
  sensitive = true
}

output "cluster_token" {
  value     = data.aws_eks_cluster_auth.main.token
  sensitive = true
}

output "oidc_provider_arn" {
  value = aws_iam_openid_connect_provider.eks.arn
}

output "oidc_provider_url" {
  description = "OIDC provider URL without https:// prefix"
  value       = replace(aws_iam_openid_connect_provider.eks.url, "https://", "")
}

output "node_group_role_arn" {
  value = aws_iam_role.node_group.arn
}

output "cluster_security_group_id" {
  value = aws_eks_cluster.main.vpc_config[0].cluster_security_group_id
}
