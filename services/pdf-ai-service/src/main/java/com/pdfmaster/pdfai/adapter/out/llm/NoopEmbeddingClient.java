package com.pdfmaster.pdfai.adapter.out.llm;

import com.pdfmaster.pdfai.adapter.out.persistence.DocumentChunkEntity;
import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link EmbeddingClient} fallback — returned by {@code AiBeans} when no real API key is
 * configured and the profile is not "test". Returns zero vectors.
 */
public class NoopEmbeddingClient implements EmbeddingClient {

  private static final Logger LOG = LoggerFactory.getLogger(NoopEmbeddingClient.class);

  @Override
  public float[] embed(String text) {
    LOG.info("NoopEmbeddingClient.embed — no OpenAI key configured");
    return new float[DocumentChunkEntity.EMBEDDING_DIMENSION];
  }

  @Override
  public List<float[]> embedBatch(List<String> texts) {
    LOG.info("NoopEmbeddingClient.embedBatch count={} — no OpenAI key configured", texts.size());
    List<float[]> result = new ArrayList<>(texts.size());
    for (int i = 0; i < texts.size(); i++) {
      result.add(new float[DocumentChunkEntity.EMBEDDING_DIMENSION]);
    }
    return result;
  }
}
