resource "aws_db_subnet_group" "main" {
  name       = "pdfmaster-${var.environment}"
  subnet_ids = var.subnet_ids
  tags       = var.tags
}

resource "aws_security_group" "rds" {
  name        = "pdfmaster-${var.environment}-rds"
  description = "Allow PostgreSQL from EKS nodes"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = var.allowed_security_group_ids
    description     = "PostgreSQL from EKS nodes"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Allow all outbound"
  }

  tags = merge(var.tags, { Name = "pdfmaster-${var.environment}-rds" })
}

resource "aws_kms_key" "rds" {
  description             = "RDS encryption key for pdfmaster-${var.environment}"
  deletion_window_in_days = 30
  enable_key_rotation     = true
  tags                    = var.tags
}

# Retrieve the master password from Secrets Manager
data "aws_secretsmanager_secret_version" "master_password" {
  secret_id = var.master_password_secret_arn
}

resource "aws_db_instance" "main" {
  identifier = "pdfmaster-${var.environment}"

  engine               = "postgres"
  engine_version       = var.engine_version
  instance_class       = var.instance_class
  allocated_storage    = var.allocated_storage
  max_allocated_storage = var.max_allocated_storage
  storage_type         = "gp3"
  storage_encrypted    = true
  kms_key_id           = aws_kms_key.rds.arn

  db_name  = "postgres"  # default DB; individual service schemas created via Flyway
  username = "pdfmaster"
  password = jsondecode(data.aws_secretsmanager_secret_version.master_password.secret_string)["password"]

  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  multi_az               = var.multi_az
  publicly_accessible    = false

  backup_retention_period = var.backup_retention_days
  backup_window           = "03:00-04:00"
  maintenance_window      = "sun:04:00-sun:05:00"

  deletion_protection      = true
  delete_automated_backups = false
  skip_final_snapshot      = false
  final_snapshot_identifier = "pdfmaster-${var.environment}-final"

  performance_insights_enabled          = true
  performance_insights_retention_period = 7
  monitoring_interval                   = 60
  monitoring_role_arn                   = aws_iam_role.rds_monitoring.arn
  enabled_cloudwatch_logs_exports       = ["postgresql", "upgrade"]

  auto_minor_version_upgrade = true
  apply_immediately          = false

  tags = var.tags

  lifecycle {
    ignore_changes = [password]  # Password managed via Secrets Manager rotation
  }
}

# --- Enhanced Monitoring IAM Role ---
resource "aws_iam_role" "rds_monitoring" {
  name = "pdfmaster-${var.environment}-rds-monitoring"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect    = "Allow"
      Principal = { Service = "monitoring.rds.amazonaws.com" }
      Action    = "sts:AssumeRole"
    }]
  })

  tags = var.tags
}

resource "aws_iam_role_policy_attachment" "rds_monitoring" {
  role       = aws_iam_role.rds_monitoring.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonRDSEnhancedMonitoringRole"
}
