locals {
  # Divide the VPC CIDR into /20 blocks — supports ~4096 IPs per subnet
  public_subnets  = [for i, az in var.availability_zones : cidrsubnet(var.vpc_cidr, 4, i)]
  private_subnets = [for i, az in var.availability_zones : cidrsubnet(var.vpc_cidr, 4, i + length(var.availability_zones))]
  db_subnets      = [for i, az in var.availability_zones : cidrsubnet(var.vpc_cidr, 4, i + 2 * length(var.availability_zones))]
}

resource "aws_vpc" "main" {
  cidr_block           = var.vpc_cidr
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = merge(var.tags, {
    Name = "pdfmaster-${var.environment}"
  })
}

# --- Internet Gateway ---
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id
  tags   = merge(var.tags, { Name = "pdfmaster-${var.environment}-igw" })
}

# --- Public Subnets ---
resource "aws_subnet" "public" {
  count                   = length(var.availability_zones)
  vpc_id                  = aws_vpc.main.id
  cidr_block              = local.public_subnets[count.index]
  availability_zone       = var.availability_zones[count.index]
  map_public_ip_on_launch = true

  tags = merge(var.tags, {
    Name                                        = "pdfmaster-${var.environment}-public-${var.availability_zones[count.index]}"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
    "kubernetes.io/role/elb"                    = "1"
  })
}

# --- Private Subnets (EKS nodes) ---
resource "aws_subnet" "private" {
  count             = length(var.availability_zones)
  vpc_id            = aws_vpc.main.id
  cidr_block        = local.private_subnets[count.index]
  availability_zone = var.availability_zones[count.index]

  tags = merge(var.tags, {
    Name                                        = "pdfmaster-${var.environment}-private-${var.availability_zones[count.index]}"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
    "kubernetes.io/role/internal-elb"           = "1"
  })
}

# --- DB Subnets (RDS, ElastiCache, Amazon MQ — isolated from EKS nodes) ---
resource "aws_subnet" "db" {
  count             = length(var.availability_zones)
  vpc_id            = aws_vpc.main.id
  cidr_block        = local.db_subnets[count.index]
  availability_zone = var.availability_zones[count.index]

  tags = merge(var.tags, {
    Name = "pdfmaster-${var.environment}-db-${var.availability_zones[count.index]}"
  })
}

# --- NAT Gateways (one per AZ for HA) ---
resource "aws_eip" "nat" {
  count  = length(var.availability_zones)
  domain = "vpc"
  tags   = merge(var.tags, { Name = "pdfmaster-${var.environment}-nat-eip-${count.index}" })
}

resource "aws_nat_gateway" "main" {
  count         = length(var.availability_zones)
  allocation_id = aws_eip.nat[count.index].id
  subnet_id     = aws_subnet.public[count.index].id
  depends_on    = [aws_internet_gateway.main]
  tags          = merge(var.tags, { Name = "pdfmaster-${var.environment}-nat-${var.availability_zones[count.index]}" })
}

# --- Route Tables ---
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
  tags = merge(var.tags, { Name = "pdfmaster-${var.environment}-public-rt" })
}

resource "aws_route_table_association" "public" {
  count          = length(var.availability_zones)
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table" "private" {
  count  = length(var.availability_zones)
  vpc_id = aws_vpc.main.id
  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.main[count.index].id
  }
  tags = merge(var.tags, { Name = "pdfmaster-${var.environment}-private-rt-${var.availability_zones[count.index]}" })
}

resource "aws_route_table_association" "private" {
  count          = length(var.availability_zones)
  subnet_id      = aws_subnet.private[count.index].id
  route_table_id = aws_route_table.private[count.index].id
}

resource "aws_route_table" "db" {
  vpc_id = aws_vpc.main.id
  # DB subnets have no default route — only local VPC traffic
  tags = merge(var.tags, { Name = "pdfmaster-${var.environment}-db-rt" })
}

resource "aws_route_table_association" "db" {
  count          = length(var.availability_zones)
  subnet_id      = aws_subnet.db[count.index].id
  route_table_id = aws_route_table.db.id
}

# --- S3 VPC Gateway Endpoint (free, keeps S3 traffic off NAT) ---
resource "aws_vpc_endpoint" "s3" {
  vpc_id            = aws_vpc.main.id
  service_name      = "com.amazonaws.${var.aws_region}.s3"
  vpc_endpoint_type = "Gateway"
  route_table_ids = concat(
    [for rt in aws_route_table.private : rt.id],
    [aws_route_table.db.id]
  )
  tags = merge(var.tags, { Name = "pdfmaster-${var.environment}-s3-endpoint" })
}

# --- Flow Logs (TODO: enable for production hardening) ---
# resource "aws_flow_log" "main" {
#   vpc_id          = aws_vpc.main.id
#   traffic_type    = "ALL"
#   iam_role_arn    = aws_iam_role.flow_log.arn
#   log_destination = aws_s3_bucket.flow_logs.arn
# }
