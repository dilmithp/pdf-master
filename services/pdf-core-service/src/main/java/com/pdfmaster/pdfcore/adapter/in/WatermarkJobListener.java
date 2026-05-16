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
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Overlays a text watermark on every page. Options: {@code text} (required), {@code
 * fontSize} (default 48), {@code opacity} (0.0–1.0, default 0.3), {@code rotation} (degrees,
 * default 45).
 */
@Component
public class WatermarkJobListener extends AbstractJobListener {

  public WatermarkJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.WATERMARK_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "watermark";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doWatermark(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] watermark(byte[] input, String text) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doWatermark(input, Map.of("text", text)).bytes();
  }

  private ProcessResult doWatermark(byte[] input, Map<String, Object> options) throws IOException {
    String text = options.get("text") instanceof String s ? s : "CONFIDENTIAL";
    float fontSize = parseFloatOption(options, "fontSize", 48f);
    float opacity = parseFloatOption(options, "opacity", 0.3f);
    float rotationDeg = parseFloatOption(options, "rotation", 45f);
    float rotationRad = (float) Math.toRadians(rotationDeg);

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
      for (int i = 0; i < doc.getNumberOfPages(); i++) {
        PDPage page = doc.getPage(i);
        PDRectangle mediaBox = page.getMediaBox();
        float cx = mediaBox.getLowerLeftX() + mediaBox.getWidth() / 2f;
        float cy = mediaBox.getLowerLeftY() + mediaBox.getHeight() / 2f;

        PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
        gs.setNonStrokingAlphaConstant(opacity);
        gs.setAlphaSourceFlag(true);

        try (PDPageContentStream cs =
            new PDPageContentStream(doc, page, AppendMode.APPEND, true, true)) {
          cs.setGraphicsStateParameters(gs);
          cs.beginText();
          cs.setFont(font, fontSize);
          cs.setNonStrokingColor(0.7f, 0.7f, 0.7f);
          float textWidth = font.getStringWidth(text) / 1000f * fontSize;
          cs.setTextMatrix(
              org.apache.pdfbox.util.Matrix.getRotateInstance(
                  rotationRad, cx - textWidth / 2f, cy));
          cs.showText(text);
          cs.endText();
        }
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return new ProcessResult(out.toByteArray(), "application/pdf", "watermarked.pdf");
    }
  }

  private static float parseFloatOption(Map<String, Object> opts, String key, float def) {
    Object v = opts.get(key);
    if (v == null) return def;
    return Float.parseFloat(v.toString());
  }
}
