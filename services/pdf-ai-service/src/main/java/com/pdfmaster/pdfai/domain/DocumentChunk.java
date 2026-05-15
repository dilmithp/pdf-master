package com.pdfmaster.pdfai.domain;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain record for a chunk of a document with its embedding. The embedding length must match the
 * model dimension declared in the migration ({@code 1536}); validated by the persistence adapter.
 */
public record DocumentChunk(UUID id, JobId jobId, String content, float[] embedding) {

  public DocumentChunk {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(jobId, "jobId");
    Objects.requireNonNull(content, "content");
    Objects.requireNonNull(embedding, "embedding");
    embedding = Arrays.copyOf(embedding, embedding.length);
  }

  @Override
  public float[] embedding() {
    return Arrays.copyOf(embedding, embedding.length);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof DocumentChunk that)) {
      return false;
    }
    return id.equals(that.id)
        && jobId.equals(that.jobId)
        && content.equals(that.content)
        && Arrays.equals(embedding, that.embedding);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, jobId, content, Arrays.hashCode(embedding));
  }

  @Override
  public String toString() {
    return "DocumentChunk[id=" + id + ", jobId=" + jobId + ", contentLen=" + content.length() + "]";
  }
}
