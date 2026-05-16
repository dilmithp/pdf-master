package com.pdfmaster.pdfai.config;

import com.pdfmaster.pdfai.application.AiChatService;
import com.pdfmaster.pdfai.application.AiExtractService;
import com.pdfmaster.pdfai.application.AiIndexService;
import com.pdfmaster.pdfai.application.AiService;
import com.pdfmaster.pdfai.application.AiSummarizeService;
import com.pdfmaster.pdfai.application.port.out.Chunker;
import com.pdfmaster.pdfai.application.port.out.DocumentChunkRepository;
import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import com.pdfmaster.pdfai.application.port.out.JobPublisher;
import com.pdfmaster.pdfai.application.port.out.JobRepository;
import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.application.port.out.NearestChunkRepository;
import com.pdfmaster.pdfai.application.port.out.ObjectStore;
import com.pdfmaster.pdfai.application.port.out.TextExtractor;
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

  @Bean
  AiIndexService aiIndexService(
      TextExtractor textExtractor,
      Chunker chunker,
      EmbeddingClient embeddingClient,
      DocumentChunkRepository chunkRepository,
      AiProperties props) {
    return new AiIndexService(textExtractor, chunker, embeddingClient, chunkRepository, props);
  }

  @Bean
  AiSummarizeService aiSummarizeService(
      DocumentChunkRepository chunkRepository, LlmClient llmClient) {
    return new AiSummarizeService(chunkRepository, llmClient);
  }

  @Bean
  AiChatService aiChatService(
      EmbeddingClient embeddingClient,
      NearestChunkRepository nearestChunkRepository,
      LlmClient llmClient,
      AiProperties props) {
    return new AiChatService(embeddingClient, nearestChunkRepository, llmClient, props);
  }

  @Bean
  AiExtractService aiExtractService(
      DocumentChunkRepository chunkRepository, LlmClient llmClient) {
    return new AiExtractService(chunkRepository, llmClient);
  }
}
