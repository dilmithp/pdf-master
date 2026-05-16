package com.pdfmaster.pdfai.adapter.out.llm;

import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.domain.AiOperation;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Test-profile {@link LlmClient} that returns deterministic canned responses without any real API
 * call. Active only under {@code @Profile("test")}.
 */
@Component
@Profile("test")
public class StubLlmClient implements LlmClient {

  private static final Logger LOG = LoggerFactory.getLogger(StubLlmClient.class);

  static final String STUB_SUMMARY = "[stub-summary] This document discusses key topics.";
  static final String STUB_CHAT = "[stub-chat] Here is the answer based on the document context.";
  static final String STUB_EXTRACT = "{\"result\":\"stub-extract\"}";

  @Override
  public LlmResponse complete(AiOperation op, String prompt) {
    int promptLen = prompt == null ? 0 : prompt.length();
    LOG.info("StubLlmClient.complete (op={}, promptChars={})", op, promptLen);
    String text =
        switch (op) {
          case CHAT -> STUB_CHAT;
          case SUMMARIZE -> STUB_SUMMARY;
          case TRANSLATE -> "[stub-translation]";
          case REDACT -> "[stub-redaction]";
        };
    return new LlmResponse(text, promptLen / 4, text.length() / 4);
  }

  @Override
  public String complete(String systemPrompt, List<Message> messages) {
    LOG.info("StubLlmClient.complete (messages={})", messages.size());
    if (systemPrompt != null && systemPrompt.contains("summarize")) {
      return STUB_SUMMARY;
    }
    if (systemPrompt != null && systemPrompt.contains("extract")) {
      return STUB_EXTRACT;
    }
    return STUB_CHAT;
  }

  @Override
  public Stream<String> completeStream(String systemPrompt, List<Message> messages) {
    LOG.info("StubLlmClient.completeStream (messages={})", messages.size());
    return Stream.of(complete(systemPrompt, messages));
  }
}
