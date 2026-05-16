package com.pdfmaster.pdfai.adapter.out.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.config.AiProperties;
import com.pdfmaster.pdfai.domain.AiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

/**
 * Production {@link LlmClient} using Anthropic's messages API via Spring's {@link RestClient}.
 * Registered only when {@code ai.anthropic-api-key} is non-empty.
 *
 * <p>Uses prompt caching ({@code cache_control: {type: ephemeral}}) on the system block to reduce
 * repeated-context costs by ~90%.
 */
public class AnthropicLlmClient implements LlmClient {

  private static final Logger LOG = LoggerFactory.getLogger(AnthropicLlmClient.class);
  private static final String BASE_URL = "https://api.anthropic.com";
  private static final String API_VERSION = "2023-06-01";
  private static final int MAX_TOKENS = 4096;

  private final RestClient restClient;
  private final AiProperties props;

  public AnthropicLlmClient(RestClient.Builder builder, AiProperties props) {
    this.props = props;
    this.restClient =
        builder
            .baseUrl(BASE_URL)
            .defaultHeader("x-api-key", props.anthropicApiKey())
            .defaultHeader("anthropic-version", API_VERSION)
            .defaultHeader("anthropic-beta", "prompt-caching-2024-07-31")
            .defaultHeader("content-type", "application/json")
            .build();
  }

  @Override
  public LlmResponse complete(AiOperation op, String prompt) {
    String systemPrompt =
        switch (op) {
          case SUMMARIZE -> "You are a precise document summarization assistant.";
          case CHAT -> "You are a helpful PDF document assistant.";
          case TRANSLATE -> "You are a professional document translator.";
          case REDACT -> "You are a document redaction assistant.";
        };
    String text = complete(systemPrompt, List.of(Message.user(prompt)));
    int rough = prompt.length() / 4;
    return new LlmResponse(text, rough, text.length() / 4);
  }

  @Override
  public String complete(String systemPrompt, List<Message> messages) {
    Map<String, Object> body = buildBody(systemPrompt, messages, false);
    JsonNode response =
        restClient.post().uri("/v1/messages").body(body).retrieve().body(JsonNode.class);
    return extractText(response);
  }

  @Override
  public Stream<String> completeStream(String systemPrompt, List<Message> messages) {
    // For non-streaming fallback in prod when SSE infrastructure is not wired, we do a blocking
    // call and wrap it. Real SSE streaming can be enabled in a follow-up by switching to
    // WebClient + text/event-stream codec.
    String full = complete(systemPrompt, messages);
    LOG.debug("AnthropicLlmClient streaming (simulated), chars={}", full.length());
    return Stream.of(full);
  }

  private Map<String, Object> buildBody(
      String systemPrompt, List<Message> messages, boolean stream) {
    List<Map<String, Object>> apiMessages = new ArrayList<>();
    for (Message m : messages) {
      apiMessages.add(Map.of("role", m.role(), "content", m.content()));
    }
    // Cached system block
    List<Map<String, Object>> systemBlocks =
        List.of(
            Map.of(
                "type", "text",
                "text", systemPrompt,
                "cache_control", Map.of("type", "ephemeral")));
    return Map.of(
        "model", props.anthropicModel(),
        "max_tokens", MAX_TOKENS,
        "system", systemBlocks,
        "messages", apiMessages,
        "stream", stream);
  }

  private static String extractText(JsonNode response) {
    if (response == null) {
      return "";
    }
    JsonNode content = response.get("content");
    if (content == null || !content.isArray() || content.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (JsonNode block : content) {
      JsonNode text = block.get("text");
      if (text != null) {
        sb.append(text.asText());
      }
    }
    return sb.toString();
  }

}
