package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.config.RabbitMqConfig;
import com.pdfmaster.pdfcore.domain.JobId;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Removes pages specified by a range string (e.g. "2,4-6"). All other pages are kept in order.
 */
@Component
public class DeletePagesJobListener extends AbstractJobListener {

  public DeletePagesJobListener(
      ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.DELETE_PAGES_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "delete-pages";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doDelete(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] deletePages(byte[] input, String ranges) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doDelete(input, Map.of("ranges", ranges)).bytes();
  }

  private ProcessResult doDelete(byte[] input, Map<String, Object> options) throws IOException {
    String rangesRaw = options.get("ranges") instanceof String s ? s : null;
    if (rangesRaw == null || rangesRaw.isBlank()) {
      throw new IllegalArgumentException("ranges option is required for delete-pages");
    }
    try (PDDocument source =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      int total = source.getNumberOfPages();
      Set<Integer> toDelete = toZeroBasedSet(SplitJobListener.parseRanges(rangesRaw, total));
      try (PDDocument out = new PDDocument()) {
        for (int i = 0; i < total; i++) {
          if (!toDelete.contains(i)) {
            out.addPage(out.importPage(source.getPage(i)));
          }
        }
        if (out.getNumberOfPages() == 0) {
          throw new IllegalArgumentException("Cannot delete all pages from the document");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        out.save(baos);
        return new ProcessResult(baos.toByteArray(), "application/pdf", "deleted.pdf");
      }
    }
  }

  private static Set<Integer> toZeroBasedSet(List<int[]> ranges) {
    Set<Integer> set = new HashSet<>();
    for (int[] r : ranges) {
      for (int i = r[0]; i <= r[1]; i++) {
        set.add(i);
      }
    }
    return set;
  }
}
