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
 * Reorders pages in a PDF according to a 1-based order array supplied in the {@code order} option.
 * For example {@code order=[3,1,2]} produces a PDF where original page 3 is first, then 1, then 2.
 */
@Component
public class ReorderPagesJobListener extends AbstractJobListener {

  public ReorderPagesJobListener(
      ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.REORDER_PAGES_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "reorder-pages";
  }

  @Override
  @SuppressWarnings("unchecked")
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doReorder(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] reorderPages(byte[] input, List<Integer> order) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doReorder(input, Map.of("order", order)).bytes();
  }

  @SuppressWarnings("unchecked")
  private ProcessResult doReorder(byte[] input, Map<String, Object> options) throws IOException {
    Object orderRaw = options.get("order");
    if (!(orderRaw instanceof List<?> orderList) || orderList.isEmpty()) {
      throw new IllegalArgumentException("order option (non-empty integer list) is required");
    }
    List<Integer> order = ((List<Object>) orderList).stream()
        .map(o -> Integer.parseInt(o.toString()))
        .toList();

    try (PDDocument source =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      int total = source.getNumberOfPages();
      try (PDDocument out = new PDDocument()) {
        for (int pageNum : order) {
          if (pageNum < 1 || pageNum > total) {
            throw new IllegalArgumentException("Page number out of range: " + pageNum);
          }
          out.addPage(out.importPage(source.getPage(pageNum - 1)));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        out.save(baos);
        return new ProcessResult(baos.toByteArray(), "application/pdf", "reordered.pdf");
      }
    }
  }
}
