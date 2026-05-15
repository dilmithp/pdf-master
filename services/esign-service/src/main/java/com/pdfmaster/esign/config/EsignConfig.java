package com.pdfmaster.esign.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wires shared beans for the e-signature service. */
@Configuration
public class EsignConfig {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
