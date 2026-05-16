package com.pdfmaster.pdfai.adapter.out.llm;

import com.pdfmaster.pdfai.adapter.out.persistence.DocumentChunkEntity;
import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Test-profile {@link EmbeddingClient} that returns deterministic hash-based float[] without any
 * real API call. Active only under {@code @Profile("test")}.
 */
@Component
@Profile("test")
public class StubEmbeddingClient implements EmbeddingClient {

  private static final Logger LOG = LoggerFactory.getLogger(StubEmbeddingClient.class);

  @Override
  public float[] embed(String text) {
    LOG.debug("StubEmbeddingClient.embed textLen={}", text == null ? 0 : text.length());
    return deterministicEmbedding(text);
  }

  @Override
  public List<float[]> embedBatch(List<String> texts) {
    LOG.debug("StubEmbeddingClient.embedBatch count={}", texts.size());
    List<float[]> result = new ArrayList<>(texts.size());
    for (String text : texts) {
      result.add(deterministicEmbedding(text));
    }
    return result;
  }

  /**
   * Produces a 1536-dim vector seeded from {@code text.hashCode()}. Each component is a bounded
   * float so the vector is valid for cosine similarity.
   */
  static float[] deterministicEmbedding(String text) {
    int dim = DocumentChunkEntity.EMBEDDING_DIMENSION;
    float[] vec = new float[dim];
    int seed = text == null ? 0 : text.hashCode();
    for (int i = 0; i < dim; i++) {
      // LCG-style spread
      seed = seed * 1664525 + 1013904223;
      vec[i] = (seed & 0x7FFF) / (float) 0x7FFF - 0.5f;
    }
    // L2 normalize so cosine == dot
    float norm = 0;
    for (float v : vec) {
      norm += v * v;
    }
    norm = (float) Math.sqrt(norm);
    if (norm > 0) {
      for (int i = 0; i < dim; i++) {
        vec[i] /= norm;
      }
    }
    return vec;
  }
}
