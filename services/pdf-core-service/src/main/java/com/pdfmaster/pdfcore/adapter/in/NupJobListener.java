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
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.util.Matrix;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * N-up imposition: tiles N source pages onto each output sheet. Supports {@code nup} values of 2
 * (two-up landscape) or 4 (four-up 2x2 grid). Option: {@code nup} (default 2).
 */
@Component
public class NupJobListener extends AbstractJobListener {

  public NupJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.NUP_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "nup";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doNup(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] nup(byte[] input, int nupValue) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doNup(input, Map.of("nup", nupValue)).bytes();
  }

  private ProcessResult doNup(byte[] input, Map<String, Object> options) throws IOException {
    int nup = parseIntOption(options, "nup", 2);
    if (nup != 2 && nup != 4) {
      throw new IllegalArgumentException("nup must be 2 or 4, got: " + nup);
    }
    int cols = 2;
    int rows = nup == 2 ? 1 : 2;
    // Sheet: landscape A4 for 2-up, portrait A4 for 4-up
    PDRectangle sheetSize =
        nup == 2
            ? new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())
            : PDRectangle.A4;
    float cellW = sheetSize.getWidth() / cols;
    float cellH = sheetSize.getHeight() / rows;

    try (PDDocument source =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      int totalSrc = source.getNumberOfPages();
      try (PDDocument out = new PDDocument()) {
        LayerUtility layerUtility = new LayerUtility(out);
        int srcIdx = 0;
        while (srcIdx < totalSrc) {
          PDPage sheet = new PDPage(sheetSize);
          out.addPage(sheet);
          try (PDPageContentStream cs =
              new PDPageContentStream(out, sheet, AppendMode.APPEND, true, true)) {
            for (int cell = 0; cell < nup && srcIdx < totalSrc; cell++, srcIdx++) {
              int col = cell % cols;
              int row = rows - 1 - (cell / cols);
              PDRectangle srcBox = source.getPage(srcIdx).getMediaBox();
              PDFormXObject form = layerUtility.importPageAsForm(source, srcIdx);
              float scaleX = cellW / srcBox.getWidth();
              float scaleY = cellH / srcBox.getHeight();
              float scale = Math.min(scaleX, scaleY);
              float tx = col * cellW + (cellW - srcBox.getWidth() * scale) / 2f;
              float ty = row * cellH + (cellH - srcBox.getHeight() * scale) / 2f;
              cs.saveGraphicsState();
              cs.transform(Matrix.getTranslateInstance(tx, ty));
              cs.transform(Matrix.getScaleInstance(scale, scale));
              cs.drawForm(form);
              cs.restoreGraphicsState();
            }
          }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        out.save(baos);
        return new ProcessResult(baos.toByteArray(), "application/pdf", "nup.pdf");
      }
    }
  }

  private static int parseIntOption(Map<String, Object> opts, String key, int def) {
    Object v = opts.get(key);
    if (v == null) return def;
    return Integer.parseInt(v.toString());
  }
}
