package com.pdfmaster.pdfconvert.config;

import com.pdfmaster.pdfconvert.application.ConvertService;
import com.pdfmaster.pdfconvert.application.port.out.JobPublisher;
import com.pdfmaster.pdfconvert.application.port.out.JobRepository;
import com.pdfmaster.pdfconvert.application.port.out.ObjectStore;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertBeans {

  @Bean
  Clock systemClock() {
    return Clock.systemUTC();
  }

  @Bean
  ConvertService convertService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    return new ConvertService(objectStore, jobRepository, jobPublisher, clock);
  }
}
