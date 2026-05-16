resource "aws_elasticache_subnet_group" "main" {
  name       = "pdfmaster-${var.environment}"
  subnet_ids = var.subnet_ids
  tags       = var.tags
}

resource "aws_security_group" "redis" {
  name        = "pdfmaster-${var.environment}-redis"
  description = "Allow Redis from EKS nodes"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    security_groups = var.allowed_security_group_ids
    description     = "Redis from EKS nodes"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.tags, { Name = "pdfmaster-${var.environment}-redis" })
}

resource "aws_kms_key" "redis" {
  description             = "ElastiCache Redis encryption for pdfmaster-${var.environment}"
  deletion_window_in_days = 30
  enable_key_rotation     = true
  tags                    = var.tags
}

data "aws_secretsmanager_secret_version" "auth_token" {
  secret_id = var.auth_token_secret_arn
}

resource "aws_elasticache_replication_group" "main" {
  replication_group_id = "pdfmaster-${var.environment}"
  description          = "pdfmaster Redis ${var.environment}"

  node_type            = var.node_type
  num_cache_clusters   = var.num_cache_nodes
  parameter_group_name = "default.redis7"
  engine_version       = var.redis_version

  subnet_group_name  = aws_elasticache_subnet_group.main.name
  security_group_ids = [aws_security_group.redis.id]

  at_rest_encryption_enabled = true
  transit_encryption_enabled = true
  auth_token                 = jsondecode(data.aws_secretsmanager_secret_version.auth_token.secret_string)["token"]
  kms_key_id                 = aws_kms_key.redis.arn

  automatic_failover_enabled = var.num_cache_nodes > 1
  multi_az_enabled           = var.num_cache_nodes > 1

  snapshot_retention_limit = 7
  snapshot_window          = "04:00-05:00"
  maintenance_window       = "sun:05:00-sun:06:00"

  auto_minor_version_upgrade = true
  apply_immediately          = false

  tags = var.tags

  lifecycle {
    ignore_changes = [auth_token]  # Managed via Secrets Manager rotation
  }
}
