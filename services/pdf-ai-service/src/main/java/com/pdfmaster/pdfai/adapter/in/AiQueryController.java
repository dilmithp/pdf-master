package com.pdfmaster.pdfai.adapter.in;

import com.pdfmaster.pdfai.application.AiChatService;
import com.pdfmaster.pdfai.application.AiExtractService;
import com.pdfmaster.pdfai.application.AiSummarizeService;
import com.pdfmaster.pdfai.application.port.out.LlmClient.Message;
import com.pdfmaster.pdfai.domain.JobId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/** Query endpoints for the RAG pipeline: summarize, chat (SSE), and structured extract. */
@RestController
@RequestMapping("/v1/ai")
public class AiQueryController {

  private final AiSummarizeService summarizeService;
  private final AiChatService chatService;
  private final AiExtractService extractService;

  public AiQueryController(
      AiSummarizeService summarizeService,
      AiChatService chatService,
      AiExtractService extractService) {
    this.summarizeService = summarizeService;
    this.chatService = chatService;
    this.extractService = extractService;
  }

  @PostMapping("/summarize")
  public ResponseEntity<SummarizeResponse> summarize(@Valid @RequestBody SummarizeRequest body) {
    JobId jobId = JobId.of(body.jobId());
    String summary = summarizeService.summarize(jobId, body.length());
    return ResponseEntity.ok(new SummarizeResponse(summary));
  }

  @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter chat(@Valid @RequestBody ChatRequest body) {
    SseEmitter emitter = new SseEmitter(120_000L);
    JobId jobId = JobId.of(body.jobId());
    List<Message> history = toMessages(body.history());
    Thread worker =
        new Thread(
            () -> {
              try (Stream<String> stream = chatService.chat(jobId, body.message(), history)) {
                stream.forEach(
                    delta -> {
                      try {
                        emitter.send(
                            SseEmitter.event().data(delta, MediaType.TEXT_PLAIN).name("delta"));
                      } catch (Exception ex) {
                        emitter.completeWithError(ex);
                      }
                    });
                emitter.complete();
              } catch (Exception ex) {
                emitter.completeWithError(ex);
              }
            });
    worker.setDaemon(true);
    worker.start();
    return emitter;
  }

  @PostMapping("/extract")
  public ResponseEntity<ExtractResponse> extract(@Valid @RequestBody ExtractRequest body) {
    JobId jobId = JobId.of(body.jobId());
    String json = extractService.extract(jobId, body.schema());
    return ResponseEntity.ok(new ExtractResponse(json));
  }

  private static List<Message> toMessages(List<HistoryEntry> history) {
    if (history == null) {
      return List.of();
    }
    return history.stream().map(e -> new Message(e.role(), e.content())).toList();
  }

  public record SummarizeRequest(@NotBlank String jobId, String length) {}

  public record SummarizeResponse(String summary) {}

  public record ChatRequest(
      @NotBlank String jobId,
      @NotBlank String message,
      List<HistoryEntry> history) {}

  public record HistoryEntry(String role, String content) {}

  public record ExtractRequest(@NotBlank String jobId, @NotBlank String schema) {}

  public record ExtractResponse(String result) {}
}
