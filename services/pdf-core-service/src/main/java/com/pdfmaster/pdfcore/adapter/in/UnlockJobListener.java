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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Decrypts a password-protected PDF. Option: {@code password} (required for encrypted files).
 * If the file is not encrypted the input is returned as-is.
 */
@Component
public class UnlockJobListener extends AbstractJobListener {

  public UnlockJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.UNLOCK_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "unlock";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doUnlock(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] unlock(byte[] input, String password) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doUnlock(input, Map.of("password", password)).bytes();
  }

  private ProcessResult doUnlock(byte[] input, Map<String, Object> options) throws IOException {
    String password = options.get("password") instanceof String s ? s : "";
    PDDocument doc;
    if (password.isBlank()) {
      doc = Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)));
    } else {
      doc =
          Loader.loadPDF(
              new RandomAccessReadBuffer(new ByteArrayInputStream(input)), password);
    }
    try {
      doc.setAllSecurityToBeRemoved(true);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return new ProcessResult(out.toByteArray(), "application/pdf", "unlocked.pdf");
    } finally {
      doc.close();
    }
  }
}
