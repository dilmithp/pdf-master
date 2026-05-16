locals {
  # Map of secret name to initial placeholder value (JSON string).
  # Real values are set via Console or CLI after first apply.
  secrets = {
    "pdfmaster/${var.environment}/auth/oauth2-jwt-key" = jsonencode({
      private_key = "PLACEHOLDER_REPLACE_AFTER_APPLY"
      public_key  = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/billing/stripe-secret-key" = jsonencode({
      secret_key = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/billing/stripe-webhook-secret" = jsonencode({
      webhook_secret = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/notification/postmark-token" = jsonencode({
      server_token = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/ai/anthropic-api-key" = jsonencode({
      api_key = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/ai/openai-api-key" = jsonencode({
      api_key = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/clerk/secret-key" = jsonencode({
      secret_key     = "PLACEHOLDER_REPLACE_AFTER_APPLY"
      publishable_key = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/rabbitmq/connection" = jsonencode({
      username = "pdfmaster"
      password = "PLACEHOLDER_REPLACE_AFTER_APPLY"
      url      = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/postgres/master-password" = jsonencode({
      password = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
    "pdfmaster/${var.environment}/redis/auth-token" = jsonencode({
      token = "PLACEHOLDER_REPLACE_AFTER_APPLY"
    })
  }
}

resource "aws_secretsmanager_secret" "main" {
  for_each = local.secrets

  name        = each.key
  description = "pdfmaster ${var.environment} secret: ${each.key}"

  kms_key_id = var.kms_key_arn != "" ? var.kms_key_arn : null

  recovery_window_in_days = 30

  tags = merge(var.tags, {
    SecretPath = each.key
  })
}

resource "aws_secretsmanager_secret_version" "main" {
  for_each = local.secrets

  secret_id     = aws_secretsmanager_secret.main[each.key].id
  secret_string = each.value

  # Ignore future changes — values are updated manually or via rotation lambda
  lifecycle {
    ignore_changes = [secret_string]
  }
}
