# Cluster auth token for Kubernetes/Helm providers
data "aws_eks_cluster_auth" "main" {
  name = aws_eks_cluster.main.name
}
