package com.pdfmaster.pdfocr.config;

import com.pdfmaster.pdfocr.application.OcrService;
import com.pdfmaster.pdfocr.application.port.out.JobPublisher;
import com.pdfmaster.pdfocr.application.port.out.JobRepository;
import com.pdfmaster.pdfocr.application.port.out.ObjectStore;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OcrBeans {

  @Bean
  Clock systemClock() {
    return Clock.systemUTC();
  }

  @Bean
  OcrService ocrService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    return new OcrService(objectStore, jobRepository, jobPublisher, clock);
  }
}
