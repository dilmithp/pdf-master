package com.pdfmaster.pdfcore.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * AWS S3 configuration. {@code endpoint}, {@code accessKey}, and {@code secretKey} are optional and
 * are only used to override defaults when targeting LocalStack in dev or in tests.
 */
@Validated
@ConfigurationProperties(prefix = "aws.s3")
public record AwsS3Properties(
    @NotBlank String bucket,
    @NotBlank String region,
    String endpoint,
    String accessKey,
    String secretKey) {}
