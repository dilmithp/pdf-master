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
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Inserts "Page X of Y" at the bottom-center of each page. Options: {@code fontSize} (default 10),
 * {@code marginBottom} (points from bottom edge, default 20).
 */
@Component
public class PageNumbersJobListener extends AbstractJobListener {

  public PageNumbersJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.PAGE_NUMBERS_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "page-numbers";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doAddPageNumbers(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] addPageNumbers(byte[] input) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doAddPageNumbers(input, Map.of()).bytes();
  }

  private ProcessResult doAddPageNumbers(byte[] input, Map<String, Object> options)
      throws IOException {
    float fontSize = parseFloatOption(options, "fontSize", 10f);
    float marginBottom = parseFloatOption(options, "marginBottom", 20f);

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      int total = doc.getNumberOfPages();
      PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
      for (int i = 0; i < total; i++) {
        PDPage page = doc.getPage(i);
        PDRectangle mediaBox = page.getMediaBox();
        String label = "Page " + (i + 1) + " of " + total;
        float textWidth = font.getStringWidth(label) / 1000f * fontSize;
        float x = (mediaBox.getWidth() - textWidth) / 2f + mediaBox.getLowerLeftX();
        float y = mediaBox.getLowerLeftY() + marginBottom;
        try (PDPageContentStream cs =
            new PDPageContentStream(doc, page, AppendMode.APPEND, true, true)) {
          cs.beginText();
          cs.setFont(font, fontSize);
          cs.newLineAtOffset(x, y);
          cs.showText(label);
          cs.endText();
        }
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return new ProcessResult(out.toByteArray(), "application/pdf", "numbered.pdf");
    }
  }

  private static float parseFloatOption(Map<String, Object> opts, String key, float def) {
    Object v = opts.get(key);
    if (v == null) return def;
    return Float.parseFloat(v.toString());
  }
}
