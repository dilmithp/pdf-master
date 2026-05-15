package com.pdfmaster.pdfconvert.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "aws.s3")
public record AwsS3Properties(
    @NotBlank String bucket,
    @NotBlank String region,
    String endpoint,
    String accessKey,
    String secretKey) {}
