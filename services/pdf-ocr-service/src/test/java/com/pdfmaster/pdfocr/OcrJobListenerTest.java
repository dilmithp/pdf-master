package com.pdfmaster.pdfocr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pdfmaster.pdfocr.adapter.in.OcrJobListener;
import com.pdfmaster.pdfocr.adapter.out.tesseract.NoopOcrEngine;
import com.pdfmaster.pdfocr.application.port.out.JobRepository;
import com.pdfmaster.pdfocr.application.port.out.ObjectStore;
import com.pdfmaster.pdfocr.application.port.out.OcrEngine;
import com.pdfmaster.pdfocr.domain.JobId;
import com.pdfmaster.pdfocr.domain.JobRecord;
import com.pdfmaster.pdfocr.domain.JobStatus;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

class OcrJobListenerTest {

  @Test
  void textModeUploadsTextFile() {
    byte[] pdf = singlePagePdf();
    UUID jobId = UUID.randomUUID();

    ObjectStore store = mock(ObjectStore.class);
    JobRepository repo = setupRepo(jobId);
    when(store.download("input.pdf")).thenReturn(pdf);

    OcrJobListener listener = new OcrJobListener(store, repo, new NoopOcrEngine(), Clock.systemUTC());

    Map<String, Object> msg = buildMessage(jobId, "input.pdf", "eng", "text");
    listener.onMessage(msg);

    verify(store).upload(
        org.mockito.ArgumentMatchers.contains("text.txt"),
        any(),
        org.mockito.ArgumentMatchers.contains("text/plain"),
        any());
  }

  @Test
  void jsonModeUploadsJsonFile() {
    byte[] pdf = singlePagePdf();
    UUID jobId = UUID.randomUUID();

    ObjectStore store = mock(ObjectStore.class);
    JobRepository repo = setupRepo(jobId);
    when(store.download("input.pdf")).thenReturn(pdf);

    OcrJobListener listener = new OcrJobListener(store, repo, new NoopOcrEngine(), Clock.systemUTC());

    Map<String, Object> msg = buildMessage(jobId, "input.pdf", "eng", "json");
    listener.onMessage(msg);

    verify(store).upload(
        org.mockito.ArgumentMatchers.contains("result.json"),
        any(),
        org.mockito.ArgumentMatchers.contains("application/json"),
        any());
  }

  @Test
  void searchablePdfModeUploadsSearchablePdf() {
    byte[] pdf = singlePagePdf();
    UUID jobId = UUID.randomUUID();

    ObjectStore store = mock(ObjectStore.class);
    JobRepository repo = setupRepo(jobId);
    when(store.download("input.pdf")).thenReturn(pdf);

    OcrJobListener listener = new OcrJobListener(store, repo, new NoopOcrEngine(), Clock.systemUTC());

    Map<String, Object> msg = buildMessage(jobId, "input.pdf", "eng", "searchable-pdf");
    listener.onMessage(msg);

    verify(store).upload(
        org.mockito.ArgumentMatchers.contains("searchable.pdf"),
        any(),
        org.mockito.ArgumentMatchers.eq("application/pdf"),
        any());
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    UUID jobId = UUID.randomUUID();
    ObjectStore store = mock(ObjectStore.class);
    when(store.download("bad.pdf")).thenReturn("not a pdf".getBytes());

    JobRepository repo = setupRepo(jobId);
    OcrJobListener listener = new OcrJobListener(store, repo, new NoopOcrEngine(), Clock.systemUTC());

    Map<String, Object> msg = buildMessage(jobId, "bad.pdf", "eng", "text");
    assertThatThrownBy(() -> listener.onMessage(msg))
        .isInstanceOf(AmqpRejectAndDontRequeueException.class);
  }

  @Test
  void rejectsMissingKey() {
    UUID jobId = UUID.randomUUID();
    JobRepository repo = setupRepo(jobId);
    OcrJobListener listener =
        new OcrJobListener(mock(ObjectStore.class), repo, new NoopOcrEngine(), Clock.systemUTC());

    Map<String, Object> msg = new HashMap<>();
    msg.put("jobId", jobId.toString());
    msg.put("inputKeys", List.of()); // empty
    msg.put("options", Map.of("language", "eng"));
    assertThatThrownBy(() -> listener.onMessage(msg))
        .isInstanceOf(AmqpRejectAndDontRequeueException.class);
  }

  // ---- helpers ----

  private static JobRepository setupRepo(UUID jobId) {
    JobRepository repo = mock(JobRepository.class);
    JobId jid = new JobId(jobId);
    JobRecord record = JobRecord.queued(jid, "ocr", List.of("input.pdf"), Instant.now());
    when(repo.findById(jid)).thenReturn(Optional.of(record));
    when(repo.markStatus(jid, JobStatus.RUNNING)).thenReturn(record.withStatus(JobStatus.RUNNING, Instant.now()));
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));
    return repo;
  }

  private static Map<String, Object> buildMessage(
      UUID jobId, String key, String language, String outputMode) {
    Map<String, Object> msg = new HashMap<>();
    msg.put("jobId", jobId.toString());
    msg.put("inputKeys", List.of(key));
    msg.put("options", Map.of("language", language, "outputMode", outputMode));
    return msg;
  }

  static byte[] singlePagePdf() {
    try (PDDocument doc = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      doc.addPage(new PDPage());
      doc.save(out);
      return out.toByteArray();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
