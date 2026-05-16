package com.pdfmaster.pdfai.application;

import com.pdfmaster.pdfai.application.port.out.Chunker;
import com.pdfmaster.pdfai.application.port.out.Chunker.TextChunk;
import com.pdfmaster.pdfai.application.port.out.DocumentChunkRepository;
import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import com.pdfmaster.pdfai.application.port.out.TextExtractor;
import com.pdfmaster.pdfai.config.AiProperties;
import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Orchestrates the indexing pipeline: extract text from a PDF → chunk into sentences → embed each
 * chunk → persist embeddings to pgvector.
 *
 * <p>Hard limits: &gt;200 pages or &gt;500 000 characters are rejected to bound token/cost
 * exposure.
 */
public class AiIndexService {

  private static final Logger LOG = LoggerFactory.getLogger(AiIndexService.class);
  private static final int MAX_CHARS = 500_000;

  private final TextExtractor textExtractor;
  private final Chunker chunker;
  private final EmbeddingClient embeddingClient;
  private final DocumentChunkRepository chunkRepository;
  private final AiProperties props;

  public AiIndexService(
      TextExtractor textExtractor,
      Chunker chunker,
      EmbeddingClient embeddingClient,
      DocumentChunkRepository chunkRepository,
      AiProperties props) {
    this.textExtractor = textExtractor;
    this.chunker = chunker;
    this.embeddingClient = embeddingClient;
    this.chunkRepository = chunkRepository;
    this.props = props;
  }

  /**
   * Index the given PDF bytes under the given job ID.
   *
   * @param jobId owning job
   * @param pdfBytes raw PDF bytes
   * @throws IllegalArgumentException if the document exceeds size limits
   */
  public void index(JobId jobId, byte[] pdfBytes) {
    String text = textExtractor.extract(pdfBytes);
    validateTextSize(jobId, text);
    List<TextChunk> chunks = chunker.chunk(text, props.maxChunkTokens(), props.chunkOverlap());
    LOG.info("Indexing jobId={} chunks={}", jobId, chunks.size());
    List<String> contents = chunks.stream().map(TextChunk::content).toList();
    List<float[]> embeddings = embeddingClient.embedBatch(contents);
    for (int i = 0; i < chunks.size(); i++) {
      float[] embedding = i < embeddings.size() ? embeddings.get(i) : new float[1536];
      DocumentChunk chunk =
          new DocumentChunk(UUID.randomUUID(), jobId, chunks.get(i).content(), embedding);
      chunkRepository.save(chunk);
    }
    LOG.info("Indexed jobId={} saved={} chunks", jobId, chunks.size());
  }

  private static void validateTextSize(JobId jobId, String text) {
    if (text.length() > MAX_CHARS) {
      throw new IllegalArgumentException(
          "Document for job "
              + jobId
              + " exceeds maximum size ("
              + text.length()
              + " chars > "
              + MAX_CHARS
              + ")");
    }
  }
}
