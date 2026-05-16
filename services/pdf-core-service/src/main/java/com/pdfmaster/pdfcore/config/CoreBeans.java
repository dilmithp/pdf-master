package com.pdfmaster.pdfcore.config;

import com.pdfmaster.pdfcore.application.CoreJobService;
import com.pdfmaster.pdfcore.application.MergeService;
import com.pdfmaster.pdfcore.application.port.out.JobPublisher;
import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wires the framework-agnostic application services from their outbound ports. */
@Configuration
public class CoreBeans {

  @Bean
  Clock systemClock() {
    return Clock.systemUTC();
  }

  @Bean
  MergeService mergeService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    return new MergeService(objectStore, jobRepository, jobPublisher, clock);
  }

  @Bean
  CoreJobService coreJobService(
      ObjectStore objectStore,
      JobRepository jobRepository,
      JobPublisher jobPublisher,
      Clock clock) {
    return new CoreJobService(objectStore, jobRepository, jobPublisher, clock);
  }
}
