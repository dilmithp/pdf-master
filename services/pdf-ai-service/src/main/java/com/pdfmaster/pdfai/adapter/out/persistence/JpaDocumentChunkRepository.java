package com.pdfmaster.pdfai.adapter.out.persistence;

import com.pdfmaster.pdfai.application.port.out.DocumentChunkRepository;
import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Postgres + pgvector backed implementation of {@link DocumentChunkRepository}. */
@Component
public class JpaDocumentChunkRepository implements DocumentChunkRepository {

  private final SpringDataDocumentChunkRepository delegate;

  public JpaDocumentChunkRepository(SpringDataDocumentChunkRepository delegate) {
    this.delegate = delegate;
  }

  @Override
  @Transactional
  public DocumentChunk save(DocumentChunk chunk) {
    DocumentChunkEntity entity =
        new DocumentChunkEntity(
            chunk.id(), chunk.jobId().value(), chunk.content(), chunk.embedding());
    delegate.save(entity);
    return chunk;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DocumentChunk> findByJobId(JobId jobId) {
    return delegate.findByJobId(jobId.value()).stream()
        .map(
            e ->
                new DocumentChunk(
                    e.getId(), new JobId(e.getJobId()), e.getContent(), e.getEmbedding()))
        .toList();
  }
}
