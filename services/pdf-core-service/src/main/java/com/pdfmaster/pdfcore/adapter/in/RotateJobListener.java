package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.config.RabbitMqConfig;
import com.pdfmaster.pdfcore.domain.JobId;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.util.Map;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Rotates pages in a PDF. Options: {@code degrees} (90|180|270, default 90), {@code pages}
 * (comma-separated 1-based page numbers or "all", default "all").
 */
@Component
public class RotateJobListener extends AbstractJobListener {

  public RotateJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.ROTATE_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "rotate";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return rotate(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] rotate(byte[] input, int degrees, String pagesOpt) throws IOException {
    PdfMagicValidator.validate("input", input);
    return rotate(input, Map.of("degrees", degrees, "pages", pagesOpt)).bytes();
  }

  private ProcessResult rotate(byte[] input, Map<String, Object> options) throws IOException {
    int degrees = parseIntOption(options, "degrees", 90);
    if (degrees != 90 && degrees != 180 && degrees != 270) {
      throw new IllegalArgumentException("degrees must be 90, 180 or 270, got: " + degrees);
    }
    String pagesOpt = options.get("pages") instanceof String s ? s : "all";
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      int total = doc.getNumberOfPages();
      for (int i = 0; i < total; i++) {
        if (shouldRotatePage(pagesOpt, i + 1)) {
          PDPage page = doc.getPage(i);
          page.setRotation((page.getRotation() + degrees) % 360);
        }
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return new ProcessResult(out.toByteArray(), "application/pdf", "rotated.pdf");
    }
  }

  private static boolean shouldRotatePage(String pagesOpt, int pageNum) {
    if ("all".equalsIgnoreCase(pagesOpt)) {
      return true;
    }
    for (String part : pagesOpt.split(",")) {
      if (Integer.parseInt(part.trim()) == pageNum) {
        return true;
      }
    }
    return false;
  }

  private static int parseIntOption(Map<String, Object> options, String key, int defaultVal) {
    Object v = options.get(key);
    if (v == null) return defaultVal;
    return Integer.parseInt(v.toString());
  }
}
