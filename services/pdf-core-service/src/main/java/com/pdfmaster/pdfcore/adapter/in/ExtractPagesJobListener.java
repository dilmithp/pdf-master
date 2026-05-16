package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.config.RabbitMqConfig;
import com.pdfmaster.pdfcore.domain.JobId;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Extracts a subset of pages defined by a range string (e.g. "1-3,5,7-9"). The extracted pages
 * are returned as a new PDF in the order they appear in the ranges list.
 */
@Component
public class ExtractPagesJobListener extends AbstractJobListener {

  public ExtractPagesJobListener(
      ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.EXTRACT_PAGES_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "extract-pages";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doExtract(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] extractPages(byte[] input, String ranges) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doExtract(input, Map.of("ranges", ranges)).bytes();
  }

  private ProcessResult doExtract(byte[] input, Map<String, Object> options) throws IOException {
    String rangesRaw = options.get("ranges") instanceof String s ? s : null;
    if (rangesRaw == null || rangesRaw.isBlank()) {
      throw new IllegalArgumentException("ranges option is required for extract-pages");
    }
    try (PDDocument source =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      int total = source.getNumberOfPages();
      List<int[]> ranges = SplitJobListener.parseRanges(rangesRaw, total);
      try (PDDocument out = new PDDocument()) {
        for (int[] range : ranges) {
          for (int p = range[0]; p <= range[1]; p++) {
            out.addPage(out.importPage(source.getPage(p)));
          }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        out.save(baos);
        return new ProcessResult(baos.toByteArray(), "application/pdf", "extracted.pdf");
      }
    }
  }
}
