package com.pdfmaster.pdfai.adapter.out.persistence;

import com.pdfmaster.pdfai.application.port.out.NearestChunkRepository;
import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Postgres + pgvector backed implementation of {@link NearestChunkRepository}. */
@Component
public class JpaNearestChunkRepository implements NearestChunkRepository {

  private final SpringDataDocumentChunkRepository delegate;

  public JpaNearestChunkRepository(SpringDataDocumentChunkRepository delegate) {
    this.delegate = delegate;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DocumentChunk> findNearest(JobId jobId, float[] queryEmbedding, int k) {
    String queryVec = toVectorLiteral(queryEmbedding);
    return delegate.findNearest(jobId.value(), queryVec, k).stream()
        .map(e -> new DocumentChunk(e.getId(), new JobId(e.getJobId()), e.getContent(), e.getEmbedding()))
        .toList();
  }

  /** Convert float[] to pgvector text literal {@code [0.1,0.2,...]}. */
  static String toVectorLiteral(float[] vec) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < vec.length; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(vec[i]);
    }
    sb.append(']');
    return sb.toString();
  }
}
