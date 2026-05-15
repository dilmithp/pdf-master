package com.pdfmaster.pdfai.config;

import com.pdfmaster.pdfai.application.AiService;
import com.pdfmaster.pdfai.application.port.out.JobPublisher;
import com.pdfmaster.pdfai.application.port.out.JobRepository;
import com.pdfmaster.pdfai.application.port.out.ObjectStore;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiBeans {

  @Bean
  Clock systemClock() {
    return Clock.systemUTC();
  }

  @Bean
  AiService aiService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    return new AiService(objectStore, jobRepository, jobPublisher, clock);
  }
}
