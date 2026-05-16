package com.pdfmaster.pdfai.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataDocumentChunkRepository
    extends JpaRepository<DocumentChunkEntity, UUID> {

  List<DocumentChunkEntity> findByJobId(UUID jobId);

  /**
   * Returns the {@code k} document chunks nearest to {@code queryVec} (cosine distance) within the
   * given job. The {@code queryVec} parameter must be a pgvector text literal, e.g.
   * {@code "[0.1,0.2,...]"}.
   */
  @Query(
      value =
          "SELECT * FROM pdf_ai.document_chunk"
              + " WHERE job_id = :jobId"
              + " ORDER BY embedding <=> CAST(:queryVec AS vector)"
              + " LIMIT :k",
      nativeQuery = true)
  List<DocumentChunkEntity> findNearest(
      @Param("jobId") UUID jobId, @Param("queryVec") String queryVec, @Param("k") int k);
}
