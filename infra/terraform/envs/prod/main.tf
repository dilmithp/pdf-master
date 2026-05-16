module "vpc" {
  source = "../../modules/vpc"

  cluster_name       = var.cluster_name
  vpc_cidr           = var.vpc_cidr
  availability_zones = var.availability_zones
  environment        = var.environment
  aws_region         = var.aws_region
  tags               = var.tags
}

module "eks" {
  source = "../../modules/eks"

  cluster_name       = var.cluster_name
  kubernetes_version = var.kubernetes_version
  vpc_id             = module.vpc.vpc_id
  subnet_ids         = module.vpc.private_subnet_ids
  node_instance_type = var.node_instance_type
  node_min_size      = var.node_min_size
  node_max_size      = var.node_max_size
  node_desired_size  = var.node_desired_size
  environment        = var.environment
  tags               = var.tags
}

module "secrets" {
  source = "../../modules/secrets"

  environment = var.environment
  tags        = var.tags
}

module "rds" {
  source = "../../modules/rds"

  cluster_name               = var.cluster_name
  environment                = var.environment
  vpc_id                     = module.vpc.vpc_id
  subnet_ids                 = module.vpc.db_subnet_ids
  allowed_security_group_ids = [module.eks.cluster_security_group_id]
  instance_class             = var.rds_instance_class
  engine_version             = var.rds_postgres_version
  allocated_storage          = var.rds_allocated_storage
  max_allocated_storage      = var.rds_max_allocated_storage
  master_password_secret_arn = module.secrets.postgres_secret_arn
  tags                       = var.tags

  depends_on = [module.secrets]
}

module "elasticache" {
  source = "../../modules/elasticache"

  cluster_name               = var.cluster_name
  environment                = var.environment
  vpc_id                     = module.vpc.vpc_id
  subnet_ids                 = module.vpc.db_subnet_ids
  allowed_security_group_ids = [module.eks.cluster_security_group_id]
  node_type                  = var.elasticache_node_type
  num_cache_nodes            = var.elasticache_num_cache_nodes
  auth_token_secret_arn      = module.secrets.redis_secret_arn
  tags                       = var.tags

  depends_on = [module.secrets]
}

module "amazon_mq" {
  source = "../../modules/amazon-mq"

  cluster_name               = var.cluster_name
  environment                = var.environment
  vpc_id                     = module.vpc.vpc_id
  subnet_ids                 = slice(module.vpc.db_subnet_ids, 0, 2)  # ACTIVE_STANDBY needs 2 subnets
  allowed_security_group_ids = [module.eks.cluster_security_group_id]
  instance_type              = var.mq_instance_type
  engine_version             = var.mq_rabbitmq_version
  deployment_mode            = "ACTIVE_STANDBY_MULTI_AZ"
  connection_secret_arn      = module.secrets.rabbitmq_secret_arn
  tags                       = var.tags

  depends_on = [module.secrets]
}

module "ecr" {
  source = "../../modules/ecr"

  services    = var.services
  account_id  = var.account_id
  environment = var.environment
  tags        = var.tags
}

module "external_secrets" {
  source = "../../modules/external-secrets"

  cluster_name      = var.cluster_name
  environment       = var.environment
  oidc_provider_arn = module.eks.oidc_provider_arn
  oidc_provider_url = module.eks.oidc_provider_url
  account_id        = var.account_id
  aws_region        = var.aws_region
  secret_arns       = values(module.secrets.secret_arns)
  tags              = var.tags

  depends_on = [module.eks, module.secrets]
}
