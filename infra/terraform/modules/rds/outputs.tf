output "endpoint" {
  value     = aws_db_instance.main.endpoint
  sensitive = true
}

output "reader_endpoint" {
  description = "Same as endpoint for single-instance RDS; use Aurora for true reader endpoint"
  value       = aws_db_instance.main.endpoint
  sensitive   = true
}

output "port" {
  value = aws_db_instance.main.port
}

output "security_group_id" {
  value = aws_security_group.rds.id
}

output "db_instance_id" {
  value = aws_db_instance.main.id
}
