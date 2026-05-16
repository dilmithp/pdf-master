package com.pdfmaster.pdfai.application.port.out;

import com.pdfmaster.pdfai.domain.AiOperation;
import java.util.List;
import java.util.stream.Stream;

/**
 * Outbound port over an LLM provider. Supports both blocking completion and streaming (SSE).
 * Implementations: {@code AnthropicLlmClient} (prod) and {@code StubLlmClient} (@Profile("test")).
 */
public interface LlmClient {

  /**
   * Returns the existing AiOperation-based completion (used by the legacy AiJobListener).
   */
  LlmResponse complete(AiOperation op, String prompt);

  /**
   * Blocking completion with explicit system prompt + message list.
   *
   * @param systemPrompt cached system context
   * @param messages conversation turns in role/content pairs
   * @return full assistant text
   */
  String complete(String systemPrompt, List<Message> messages);

  /**
   * Streaming completion. Each element is a text delta. Callers write deltas to an SSE stream.
   *
   * @param systemPrompt cached system context
   * @param messages conversation turns in role/content pairs
   * @return lazy stream of text deltas; caller must close
   */
  Stream<String> completeStream(String systemPrompt, List<Message> messages);

  record LlmResponse(String text, int tokensIn, int tokensOut) {}

  /** A single conversation turn. */
  record Message(String role, String content) {
    public static Message user(String content) {
      return new Message("user", content);
    }

    public static Message assistant(String content) {
      return new Message("assistant", content);
    }
  }
}
