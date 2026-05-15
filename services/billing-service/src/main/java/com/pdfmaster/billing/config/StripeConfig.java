package com.pdfmaster.billing.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/** Initializes the static Stripe SDK with the configured API key. */
@Configuration
public class StripeConfig {

  private final StripeProperties properties;

  public StripeConfig(StripeProperties properties) {
    this.properties = properties;
  }

  @PostConstruct
  void init() {
    Stripe.apiKey = properties.apiKey();
  }
}
