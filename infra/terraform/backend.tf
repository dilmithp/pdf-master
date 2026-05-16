# Remote state — S3 backend with DynamoDB locking.
# Create the bucket and table before running terraform init:
#
#   aws s3api create-bucket --bucket pdfmaster-tf-state-ACCOUNT_ID --region us-east-1
#   aws s3api put-bucket-versioning --bucket pdfmaster-tf-state-ACCOUNT_ID \
#       --versioning-configuration Status=Enabled
#   aws s3api put-bucket-encryption --bucket pdfmaster-tf-state-ACCOUNT_ID \
#       --server-side-encryption-configuration \
#       '{"Rules":[{"ApplyServerSideEncryptionByDefault":{"SSEAlgorithm":"AES256"}}]}'
#   aws dynamodb create-table --table-name pdfmaster-tf-locks \
#       --attribute-definitions AttributeName=LockID,AttributeType=S \
#       --key-schema AttributeName=LockID,KeyType=HASH \
#       --billing-mode PAY_PER_REQUEST --region us-east-1
#
# Then initialise with the env-specific backend config:
#   terraform init -backend-config=envs/prod/backend.hcl
#
terraform {
  backend "s3" {
    # Values are supplied via -backend-config=envs/prod/backend.hcl at init time
  }
}
