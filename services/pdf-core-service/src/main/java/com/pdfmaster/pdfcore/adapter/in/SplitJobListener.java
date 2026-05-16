package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.config.RabbitMqConfig;
import com.pdfmaster.pdfcore.domain.JobId;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.time.Clock;
import java.util.zip.ZipOutputStream;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Splits a PDF into individual single-page PDFs packaged as a ZIP archive. Supports splitting by
 * individual pages (default) or by page ranges via the {@code ranges} option (e.g. "1-3,5,7-9").
 */
@Component
public class SplitJobListener extends AbstractJobListener {

  public SplitJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.SPLIT_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "split";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return split(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] splitToZip(byte[] input, String rangesRaw) throws IOException {
    PdfMagicValidator.validate("input", input);
    return split(input, rangesRaw == null ? Map.of() : Map.of("ranges", rangesRaw)).bytes();
  }

  private ProcessResult split(byte[] input, Map<String, Object> options) throws IOException {
    String rangesRaw = options.get("ranges") instanceof String s ? s : null;
    try (PDDocument source =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      int total = source.getNumberOfPages();
      List<int[]> ranges = parseRanges(rangesRaw, total);
      ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
      try (ZipOutputStream zip = new ZipOutputStream(zipOut)) {
        for (int[] range : ranges) {
          ByteArrayOutputStream partOut = new ByteArrayOutputStream();
          try (PDDocument part = new PDDocument()) {
            for (int p = range[0]; p <= range[1]; p++) {
              part.addPage(part.importPage(source.getPage(p)));
            }
            part.save(partOut);
          }
          String name =
              "page_" + (range[0] + 1)
                  + (range[0] == range[1] ? "" : "-" + (range[1] + 1))
                  + ".pdf";
          zip.putNextEntry(new ZipEntry(name));
          zip.write(partOut.toByteArray());
          zip.closeEntry();
        }
      }
      return new ProcessResult(zipOut.toByteArray(), "application/zip", "split.zip");
    }
  }

  /** Parses "1-3,5,7-9" into zero-based page ranges. Defaults to one range per page. */
  public static List<int[]> parseRanges(String raw, int totalPages) {
    if (raw == null || raw.isBlank()) {
      List<int[]> all = new ArrayList<>(totalPages);
      for (int i = 0; i < totalPages; i++) {
        all.add(new int[]{i, i});
      }
      return all;
    }
    List<int[]> result = new ArrayList<>();
    for (String part : raw.split(",")) {
      String trimmed = part.trim();
      if (trimmed.contains("-")) {
        String[] bounds = trimmed.split("-", 2);
        int from = Integer.parseInt(bounds[0].trim()) - 1;
        int to = Integer.parseInt(bounds[1].trim()) - 1;
        if (from < 0 || to >= totalPages || from > to) {
          throw new IllegalArgumentException("Invalid page range: " + trimmed);
        }
        result.add(new int[]{from, to});
      } else {
        int page = Integer.parseInt(trimmed) - 1;
        if (page < 0 || page >= totalPages) {
          throw new IllegalArgumentException("Page out of range: " + trimmed);
        }
        result.add(new int[]{page, page});
      }
    }
    return result;
  }
}
