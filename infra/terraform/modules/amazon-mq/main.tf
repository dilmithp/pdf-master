resource "aws_security_group" "mq" {
  name        = "pdfmaster-${var.environment}-mq"
  description = "Allow AMQP/AMQPS from EKS nodes"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 5671
    to_port         = 5671
    protocol        = "tcp"
    security_groups = var.allowed_security_group_ids
    description     = "AMQPS from EKS nodes"
  }

  ingress {
    from_port       = 443
    to_port         = 443
    protocol        = "tcp"
    security_groups = var.allowed_security_group_ids
    description     = "RabbitMQ management console HTTPS"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.tags, { Name = "pdfmaster-${var.environment}-mq" })
}

data "aws_secretsmanager_secret_version" "mq_creds" {
  secret_id = var.connection_secret_arn
}

locals {
  mq_creds = jsondecode(data.aws_secretsmanager_secret_version.mq_creds.secret_string)
}

resource "aws_mq_broker" "main" {
  broker_name        = "pdfmaster-${var.environment}"
  engine_type        = "RabbitMQ"
  engine_version     = var.engine_version
  host_instance_type = var.instance_type
  deployment_mode    = var.deployment_mode

  subnet_ids         = var.subnet_ids
  security_groups    = [aws_security_group.mq.id]
  publicly_accessible = false

  # TLS enforced — AMQPS only
  auto_minor_version_upgrade = true

  user {
    username = local.mq_creds["username"]
    password = local.mq_creds["password"]
  }

  logs {
    general = true
  }

  maintenance_window_start_time {
    day_of_week = "SUNDAY"
    time_of_day = "06:00"
    time_zone   = "UTC"
  }

  tags = var.tags

  lifecycle {
    ignore_changes = [user]  # Credentials managed via Secrets Manager
  }
}
