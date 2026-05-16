package com.pdfmaster.pdfai.adapter.out.llm;

import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.domain.AiOperation;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link LlmClient} fallback — returned by {@code AiBeans} when no real API key is
 * configured and the profile is not "test". Logs and returns canned responses.
 */
public class NoopLlmClient implements LlmClient {

  private static final Logger LOG = LoggerFactory.getLogger(NoopLlmClient.class);

  @Override
  public LlmResponse complete(AiOperation op, String prompt) {
    int promptLength = prompt == null ? 0 : prompt.length();
    LOG.info("NoopLlmClient.complete (op={}, promptChars={})", op, promptLength);
    String text =
        switch (op) {
          case CHAT -> "[noop-chat] (real LLM client not configured)";
          case SUMMARIZE -> "[noop-summary]";
          case TRANSLATE -> "[noop-translation]";
          case REDACT -> "[noop-redaction]";
        };
    return new LlmResponse(text, promptLength, text.length());
  }

  @Override
  public String complete(String systemPrompt, List<Message> messages) {
    LOG.info("NoopLlmClient.complete (messages={})", messages == null ? 0 : messages.size());
    return "[noop-response] LLM not configured.";
  }

  @Override
  public Stream<String> completeStream(String systemPrompt, List<Message> messages) {
    LOG.info(
        "NoopLlmClient.completeStream (messages={})", messages == null ? 0 : messages.size());
    return Stream.of("[noop-stream] LLM not configured.");
  }
}
