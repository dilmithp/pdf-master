output "vpc_id" {
  description = "VPC ID"
  value       = aws_vpc.main.id
}

output "vpc_cidr" {
  description = "VPC CIDR block"
  value       = aws_vpc.main.cidr_block
}

output "public_subnet_ids" {
  description = "Public subnet IDs"
  value       = aws_subnet.public[*].id
}

output "private_subnet_ids" {
  description = "Private subnet IDs (EKS nodes)"
  value       = aws_subnet.private[*].id
}

output "db_subnet_ids" {
  description = "DB subnet IDs (RDS, ElastiCache, Amazon MQ)"
  value       = aws_subnet.db[*].id
}

output "s3_endpoint_id" {
  description = "S3 VPC gateway endpoint ID"
  value       = aws_vpc_endpoint.s3.id
}
