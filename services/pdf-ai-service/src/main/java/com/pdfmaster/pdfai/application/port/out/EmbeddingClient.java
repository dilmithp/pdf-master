package com.pdfmaster.pdfai.application.port.out;

import java.util.List;

/**
 * Outbound port for generating text embeddings. Implementations: {@code OpenAiEmbeddingClient}
 * (prod) and {@code StubEmbeddingClient} (@Profile("test")).
 */
public interface EmbeddingClient {

  /** Embed a single text string. */
  float[] embed(String text);

  /**
   * Embed a batch of texts. Implementations may batch into provider-level requests (up to 100 for
   * OpenAI).
   */
  List<float[]> embedBatch(List<String> texts);
}
