# Supply at init time: terraform init -backend-config=envs/prod/backend.hcl
bucket         = "pdfmaster-tf-state-ACCOUNT_ID"
key            = "prod/terraform.tfstate"
region         = "us-east-1"
encrypt        = true
dynamodb_table = "pdfmaster-tf-locks"
