package com.pdfmaster.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/** Shared RestClient builder for outbound HTTP integrations. */
@Configuration
public class HttpConfig {

  @Bean
  public RestClient.Builder restClientBuilder() {
    return RestClient.builder();
  }
}
