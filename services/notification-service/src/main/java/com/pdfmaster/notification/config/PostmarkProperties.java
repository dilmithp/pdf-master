package com.pdfmaster.notification.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** Postmark client configuration. */
@Validated
@ConfigurationProperties(prefix = "postmark")
public record PostmarkProperties(
    @NotBlank String serverToken, @Email @NotBlank String fromAddress, @NotBlank String baseUrl) {}
