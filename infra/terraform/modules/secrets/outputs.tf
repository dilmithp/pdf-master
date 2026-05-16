output "secret_arns" {
  description = "Map of secret path to ARN"
  value       = { for k, v in aws_secretsmanager_secret.main : k => v.arn }
}

output "postgres_secret_arn" {
  value = aws_secretsmanager_secret.main["pdfmaster/${var.environment}/postgres/master-password"].arn
}

output "redis_secret_arn" {
  value = aws_secretsmanager_secret.main["pdfmaster/${var.environment}/redis/auth-token"].arn
}

output "rabbitmq_secret_arn" {
  value = aws_secretsmanager_secret.main["pdfmaster/${var.environment}/rabbitmq/connection"].arn
}
