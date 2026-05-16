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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Crops all pages by setting the CropBox. Options: {@code top}, {@code right}, {@code bottom},
 * {@code left} (margin in PDF points to trim from each edge, default 0). Alternatively accepts
 * {@code x}, {@code y}, {@code width}, {@code height} for an explicit crop box.
 */
@Component
public class CropJobListener extends AbstractJobListener {

  public CropJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.CROP_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "crop";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doCrop(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] crop(byte[] input, float top, float right, float bottom, float left)
      throws IOException {
    PdfMagicValidator.validate("input", input);
    return doCrop(input, Map.of("top", top, "right", right, "bottom", bottom, "left", left))
        .bytes();
  }

  private ProcessResult doCrop(byte[] input, Map<String, Object> options) throws IOException {
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      for (int i = 0; i < doc.getNumberOfPages(); i++) {
        PDPage page = doc.getPage(i);
        PDRectangle mediaBox = page.getMediaBox();
        PDRectangle cropBox = buildCropBox(options, mediaBox);
        page.setCropBox(cropBox);
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return new ProcessResult(out.toByteArray(), "application/pdf", "cropped.pdf");
    }
  }

  private static PDRectangle buildCropBox(Map<String, Object> options, PDRectangle mediaBox) {
    if (options.containsKey("x") || options.containsKey("width")) {
      float x = parseFloat(options, "x", mediaBox.getLowerLeftX());
      float y = parseFloat(options, "y", mediaBox.getLowerLeftY());
      float width = parseFloat(options, "width", mediaBox.getWidth());
      float height = parseFloat(options, "height", mediaBox.getHeight());
      return new PDRectangle(x, y, width, height);
    }
    float top = parseFloat(options, "top", 0f);
    float right = parseFloat(options, "right", 0f);
    float bottom = parseFloat(options, "bottom", 0f);
    float left = parseFloat(options, "left", 0f);
    float x = mediaBox.getLowerLeftX() + left;
    float y = mediaBox.getLowerLeftY() + bottom;
    float width = mediaBox.getWidth() - left - right;
    float height = mediaBox.getHeight() - top - bottom;
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Crop margins exceed page dimensions");
    }
    return new PDRectangle(x, y, width, height);
  }

  private static float parseFloat(Map<String, Object> opts, String key, float def) {
    Object v = opts.get(key);
    if (v == null) return def;
    return Float.parseFloat(v.toString());
  }
}
