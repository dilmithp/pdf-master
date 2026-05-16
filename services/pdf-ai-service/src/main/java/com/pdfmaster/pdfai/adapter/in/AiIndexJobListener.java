package com.pdfmaster.pdfai.adapter.in;

import com.pdfmaster.pdfai.application.AiIndexService;
import com.pdfmaster.pdfai.application.port.out.JobRepository;
import com.pdfmaster.pdfai.application.port.out.ObjectStore;
import com.pdfmaster.pdfai.domain.JobId;
import com.pdfmaster.pdfai.domain.JobStatus;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@code pdf.ai.index.requested}. Downloads the PDF from S3, delegates to {@link
 * AiIndexService} (extract → chunk → embed → persist), and marks the job SUCCEEDED.
 */
@Component
public class AiIndexJobListener {

  private static final Logger LOG = LoggerFactory.getLogger(AiIndexJobListener.class);

  private final ObjectStore objectStore;
  private final JobRepository jobRepository;
  private final AiIndexService aiIndexService;
  private final Clock clock;

  public AiIndexJobListener(
      ObjectStore objectStore,
      JobRepository jobRepository,
      AiIndexService aiIndexService,
      Clock clock) {
    this.objectStore = objectStore;
    this.jobRepository = jobRepository;
    this.aiIndexService = aiIndexService;
    this.clock = clock;
  }

  @RabbitListener(queues = "pdf.ai.index.requested")
  public void onMessage(Map<String, Object> message) {
    Objects.requireNonNull(message, "message");
    JobId jobId = readJobId(message);
    @SuppressWarnings("unchecked")
    List<String> keys = (List<String>) message.get("inputKeys");
    if (keys == null || keys.size() != 1) {
      fail(jobId, "Index expects exactly one input key");
      throw new AmqpRejectAndDontRequeueException("Invalid index payload: " + message);
    }
    try {
      jobRepository.markStatus(jobId, JobStatus.RUNNING);
      byte[] pdfBytes = objectStore.download(keys.get(0));
      aiIndexService.index(jobId, pdfBytes);
      jobRepository.save(
          jobRepository.findById(jobId).orElseThrow().withResult("indexed/" + jobId, clock.instant()));
      LOG.info("AI index job {} succeeded", jobId);
    } catch (RuntimeException ex) {
      LOG.error("AI index job {} failed", jobId, ex);
      fail(jobId, ex.getMessage());
      throw new AmqpRejectAndDontRequeueException("index failed for job " + jobId, ex);
    }
  }

  private void fail(JobId jobId, String message) {
    jobRepository
        .findById(jobId)
        .ifPresent(
            j ->
                jobRepository.save(
                    j.withFailure(message == null ? "unknown" : message, clock.instant())));
  }

  private static JobId readJobId(Map<String, Object> message) {
    Object raw = message.get("jobId");
    if (raw == null) {
      throw new AmqpRejectAndDontRequeueException("Missing jobId in index payload");
    }
    return new JobId(UUID.fromString(raw.toString()));
  }
}
