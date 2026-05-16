package com.pdfmaster.pdfai.application.port.out;

import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;

/**
 * Outbound port for nearest-neighbour chunk retrieval via pgvector cosine distance. Separate from
 * {@link DocumentChunkRepository} to keep the persistence adapter testable.
 */
public interface NearestChunkRepository {

  /**
   * Retrieve the {@code k} chunks most similar to {@code queryEmbedding} within the given job.
   *
   * @param jobId scope of the search
   * @param queryEmbedding the query embedding vector
   * @param k number of nearest neighbours to return
   * @return chunks ordered by ascending cosine distance; may be shorter than {@code k}
   */
  List<DocumentChunk> findNearest(JobId jobId, float[] queryEmbedding, int k);
}
