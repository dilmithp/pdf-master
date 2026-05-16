output "broker_id" {
  value = aws_mq_broker.main.id
}

output "broker_endpoint" {
  description = "AMQPS endpoint for application connection"
  value       = aws_mq_broker.main.instances[0].endpoints[0]
  sensitive   = true
}

output "broker_arn" {
  value = aws_mq_broker.main.arn
}

output "security_group_id" {
  value = aws_security_group.mq.id
}
