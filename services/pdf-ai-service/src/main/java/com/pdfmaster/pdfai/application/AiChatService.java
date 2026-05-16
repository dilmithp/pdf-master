package com.pdfmaster.pdfai.application;

import com.pdfmaster.pdfai.application.port.out.EmbeddingClient;
import com.pdfmaster.pdfai.application.port.out.LlmClient;
import com.pdfmaster.pdfai.application.port.out.LlmClient.Message;
import com.pdfmaster.pdfai.application.port.out.NearestChunkRepository;
import com.pdfmaster.pdfai.config.AiProperties;
import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Retrieval-augmented generation chat. Embeds the user message, retrieves top-K chunks via cosine
 * similarity, and streams the Claude response.
 */
public class AiChatService {

  private static final Logger LOG = LoggerFactory.getLogger(AiChatService.class);
  private static final AtomicLong QUERY_COUNTER = new AtomicLong();
  private static final int DAILY_QUOTA_LOG_THRESHOLD = 50;

  private static final String SYSTEM_PROMPT =
      "You are a helpful PDF document assistant. Answer questions using only the document "
          + "context provided below. If the answer is not in the context, say so honestly.";

  private final EmbeddingClient embeddingClient;
  private final NearestChunkRepository nearestChunkRepository;
  private final LlmClient llmClient;
  private final AiProperties props;

  public AiChatService(
      EmbeddingClient embeddingClient,
      NearestChunkRepository nearestChunkRepository,
      LlmClient llmClient,
      AiProperties props) {
    this.embeddingClient = embeddingClient;
    this.nearestChunkRepository = nearestChunkRepository;
    this.llmClient = llmClient;
    this.props = props;
  }

  /**
   * Stream an answer to {@code userMessage} grounded in the document's persisted chunks.
   *
   * @param jobId job whose chunks to retrieve from
   * @param userMessage the user's question
   * @param history prior conversation turns (alternating user/assistant)
   * @return lazy stream of text deltas; caller must consume and close
   */
  public Stream<String> chat(JobId jobId, String userMessage, List<Message> history) {
    long count = QUERY_COUNTER.incrementAndGet();
    if (count % DAILY_QUOTA_LOG_THRESHOLD == 0) {
      LOG.warn(
          "High query count for jobId={} (total={}). Wire billing to enforce per-jobId quotas.",
          jobId,
          count);
    }
    float[] queryEmbedding = embeddingClient.embed(userMessage);
    List<DocumentChunk> chunks = nearestChunkRepository.findNearest(jobId, queryEmbedding, props.topK());
    LOG.info("Chat jobId={} retrieved={} chunks", jobId, chunks.size());
    String context = chunks.stream().map(DocumentChunk::content).collect(Collectors.joining("\n\n"));
    String systemWithContext = SYSTEM_PROMPT + "\n\n---DOCUMENT CONTEXT---\n" + context;
    List<Message> messages = buildMessages(history, userMessage);
    return llmClient.completeStream(systemWithContext, messages);
  }

  private static List<Message> buildMessages(List<Message> history, String userMessage) {
    List<Message> messages = new ArrayList<>();
    if (history != null) {
      messages.addAll(history);
    }
    messages.add(Message.user(userMessage));
    return messages;
  }
}
