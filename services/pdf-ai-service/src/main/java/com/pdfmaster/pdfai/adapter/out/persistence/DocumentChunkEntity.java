package com.pdfmaster.pdfai.adapter.out.persistence;

import com.pgvector.PGvector;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import org.hibernate.annotations.Type;

/**
 * JPA mapping for a chunk of a document with its embedding. The embedding column is the
 * pgvector-managed {@code vector(1536)} type, bound via {@link PGvectorUserType} which sends the
 * value as a text literal cast to {@code vector} on the Postgres side.
 */
@Entity
@Table(name = "document_chunk", schema = "pdf_ai")
public class DocumentChunkEntity {

  /** Embedding dimension matching the migration ({@code vector(1536)}). */
  public static final int EMBEDDING_DIMENSION = 1536;

  @Id private UUID id;

  @Column(name = "job_id", nullable = false)
  private UUID jobId;

  @Column(nullable = false, columnDefinition = "text")
  private String content;

  @Type(PGvectorUserType.class)
  @Column(nullable = false, columnDefinition = "vector(1536)")
  private PGvector embedding;

  protected DocumentChunkEntity() {}

  public DocumentChunkEntity(UUID id, UUID jobId, String content, float[] embedding) {
    if (embedding == null || embedding.length != EMBEDDING_DIMENSION) {
      throw new IllegalArgumentException(
          "Embedding must be exactly " + EMBEDDING_DIMENSION + " dimensions");
    }
    this.id = id;
    this.jobId = jobId;
    this.content = content;
    this.embedding = new PGvector(embedding);
  }

  public UUID getId() {
    return id;
  }

  public UUID getJobId() {
    return jobId;
  }

  public String getContent() {
    return content;
  }

  public float[] getEmbedding() {
    return embedding == null ? new float[0] : embedding.toArray();
  }
}
