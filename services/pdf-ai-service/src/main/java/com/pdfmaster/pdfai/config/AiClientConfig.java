package com.pdfmaster.pdfai.config;

import com.pdfmaster.pdfai.adapter.out.llm.AnthropicLlmClient;
import com.pdfmaster.pdfai.adapter.out.llm.NoopEmbeddingClient;
import com.pdfmaster.pdfai.adapter.out.llm.NoopLlmClient;
import com.pdfmaster.pdfai.adapter.out.llm.OpenAiEmbeddingClient;
import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import com.pdfmaster.pdfai.application.port.out.LlmClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

/**
 * Registers production API-backed or noop-fallback implementations of {@link LlmClient} and
 * {@link EmbeddingClient}. Only active outside the "test" profile — the test profile uses
 * {@code StubLlmClient} / {@code StubEmbeddingClient} (both {@code @Component @Profile("test")}).
 *
 * <p>Selection logic (in evaluation order):
 * <ol>
 *   <li>{@code AnthropicLlmClient} — when {@code ai.anthropic-api-key} is non-empty
 *   <li>{@code NoopLlmClient} — fallback when key is absent
 *   <li>{@code OpenAiEmbeddingClient} — when {@code ai.openai-api-key} is non-empty
 *   <li>{@code NoopEmbeddingClient} — fallback when key is absent
 * </ol>
 */
@Configuration
@Profile("!test")
public class AiClientConfig {

  @Bean
  @ConditionalOnExpression("!'${ai.anthropic-api-key:}'.isEmpty()")
  LlmClient anthropicLlmClient(RestClient.Builder builder, AiProperties props) {
    return new AnthropicLlmClient(builder, props);
  }

  @Bean
  @ConditionalOnMissingBean(LlmClient.class)
  LlmClient noopLlmClient() {
    return new NoopLlmClient();
  }

  @Bean
  @ConditionalOnExpression("!'${ai.openai-api-key:}'.isEmpty()")
  EmbeddingClient openAiEmbeddingClient(RestClient.Builder builder, AiProperties props) {
    return new OpenAiEmbeddingClient(builder, props);
  }

  @Bean
  @ConditionalOnMissingBean(EmbeddingClient.class)
  EmbeddingClient noopEmbeddingClient() {
    return new NoopEmbeddingClient();
  }
}
