package com.pdfmaster.pdfai.application;

import com.pdfmaster.pdfai.application.port.out.DocumentChunkRepository;
import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.application.port.out.LlmClient.Message;
import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Structured data extraction from a document using Claude's tool-use / JSON mode. The caller
 * supplies a JSON schema string; the result is a JSON string conforming to that schema.
 */
public class AiExtractService {

  private static final Logger LOG = LoggerFactory.getLogger(AiExtractService.class);

  private static final String SYSTEM_PROMPT =
      "You are a precise data extraction assistant. "
          + "Extract the requested data from the document context and return ONLY valid JSON "
          + "conforming to the provided schema. Do not include any explanation outside the JSON.";

  private final DocumentChunkRepository chunkRepository;
  private final LlmClient llmClient;

  public AiExtractService(DocumentChunkRepository chunkRepository, LlmClient llmClient) {
    this.chunkRepository = chunkRepository;
    this.llmClient = llmClient;
  }

  /**
   * Extract structured data from the document.
   *
   * @param jobId owning job
   * @param jsonSchema JSON Schema string describing the expected output structure
   * @return JSON string conforming to the schema
   */
  public String extract(JobId jobId, String jsonSchema) {
    List<DocumentChunk> chunks = chunkRepository.findByJobId(jobId);
    LOG.info("Extract jobId={} chunks={}", jobId, chunks.size());
    String context =
        chunks.stream().map(DocumentChunk::content).collect(Collectors.joining("\n\n"));
    String userPrompt =
        "Extract data from the following document text and return JSON conforming to this schema:\n"
            + jsonSchema
            + "\n\nDocument text:\n"
            + context;
    return llmClient.complete(SYSTEM_PROMPT + " extract", List.of(Message.user(userPrompt)));
  }
}
