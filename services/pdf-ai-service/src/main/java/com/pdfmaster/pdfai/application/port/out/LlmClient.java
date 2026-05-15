package com.pdfmaster.pdfai.application.port.out;

import com.pdfmaster.pdfai.domain.AiOperation;

/**
 * Outbound port over an LLM provider. The default implementation, {@code NoopLlmClient}, returns a
 * canned response so the rest of the pipeline can be exercised without a vendor SDK on the
 * classpath.
 */
public interface LlmClient {

  LlmResponse complete(AiOperation op, String prompt);

  record LlmResponse(String text, int tokensIn, int tokensOut) {}
}
