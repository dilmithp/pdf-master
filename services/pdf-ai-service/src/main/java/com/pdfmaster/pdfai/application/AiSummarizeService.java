package com.pdfmaster.pdfai.application;

import com.pdfmaster.pdfai.application.port.out.DocumentChunkRepository;
import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.application.port.out.LlmClient.Message;
import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Fetches all persisted chunks for a job, assembles context, and calls the LLM for a summary. */
public class AiSummarizeService {

  private static final Logger LOG = LoggerFactory.getLogger(AiSummarizeService.class);

  private static final String SYSTEM_PROMPT =
      "You are a precise document summarization assistant. "
          + "Given the document text below, produce a clear, accurate summary "
          + "that preserves all key facts, numbers, and named entities.";

  private final DocumentChunkRepository chunkRepository;
  private final LlmClient llmClient;

  public AiSummarizeService(DocumentChunkRepository chunkRepository, LlmClient llmClient) {
    this.chunkRepository = chunkRepository;
    this.llmClient = llmClient;
  }

  /**
   * Summarize the document associated with {@code jobId}.
   *
   * @param jobId the job whose chunks to summarize
   * @param length "short", "medium", or "long" — appended to the prompt as a hint
   * @return summary text; non-empty even on empty documents
   */
  public String summarize(JobId jobId, String length) {
    List<DocumentChunk> chunks = chunkRepository.findByJobId(jobId);
    LOG.info("Summarize jobId={} chunks={} length={}", jobId, chunks.size(), length);
    String context = chunks.stream().map(DocumentChunk::content).collect(Collectors.joining("\n\n"));
    String userPrompt =
        "Please provide a " + sanitizeLength(length) + " summary of the following document:\n\n"
            + context;
    return llmClient.complete(SYSTEM_PROMPT, List.of(Message.user(userPrompt)));
  }

  private static String sanitizeLength(String length) {
    if (length == null) {
      return "medium";
    }
    return switch (length.toLowerCase(Locale.ROOT)) {
      case "short", "medium", "long" -> length.toLowerCase(Locale.ROOT);
      default -> "medium";
    };
  }
}
