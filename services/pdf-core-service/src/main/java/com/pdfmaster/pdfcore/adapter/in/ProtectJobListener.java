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
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Encrypts a PDF with a user password and an optional owner password. Options: {@code userPassword}
 * (required), {@code ownerPassword} (defaults to userPassword), {@code keyLength} (40|128|256,
 * default 256).
 */
@Component
public class ProtectJobListener extends AbstractJobListener {

  public ProtectJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.PROTECT_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "protect";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doProtect(input, options);
  }

  /** Public for unit-test visibility. */
  public byte[] protect(byte[] input, String userPassword) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doProtect(input, Map.of("userPassword", userPassword)).bytes();
  }

  private ProcessResult doProtect(byte[] input, Map<String, Object> options) throws IOException {
    String userPwd = options.get("userPassword") instanceof String s ? s : "";
    if (userPwd.isBlank()) {
      throw new IllegalArgumentException("userPassword is required for the protect operation");
    }
    String ownerPwd = options.get("ownerPassword") instanceof String s ? s : userPwd;
    int keyLength = parseIntOption(options, "keyLength", 256);

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(input)))) {
      AccessPermission ap = new AccessPermission();
      StandardProtectionPolicy policy =
          new StandardProtectionPolicy(ownerPwd, userPwd, ap);
      policy.setEncryptionKeyLength(keyLength);
      doc.protect(policy);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      doc.save(out);
      return new ProcessResult(out.toByteArray(), "application/pdf", "protected.pdf");
    }
  }

  private static int parseIntOption(Map<String, Object> opts, String key, int def) {
    Object v = opts.get(key);
    if (v == null) return def;
    return Integer.parseInt(v.toString());
  }
}
