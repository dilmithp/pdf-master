package com.pdfmaster.pdfcore.adapter.in;

import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import com.pdfmaster.pdfcore.config.RabbitMqConfig;
import com.pdfmaster.pdfcore.domain.JobId;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Compresses a PDF using qpdf {@code --optimize-images}. Falls back to returning the original bytes
 * unchanged if qpdf is not on the host PATH (so unit tests pass without the binary).
 */
@Component
public class CompressJobListener extends AbstractJobListener {

  private static final Logger LOG = LoggerFactory.getLogger(CompressJobListener.class);
  private static final long TIMEOUT_SEC = 120;

  public CompressJobListener(ObjectStore objectStore, JobRepository jobRepository, Clock clock) {
    super(objectStore, jobRepository, clock);
  }

  @RabbitListener(queues = RabbitMqConfig.COMPRESS_QUEUE)
  public void onMessage(Map<String, Object> message) {
    handle(message);
  }

  @Override
  protected String opName() {
    return "compress";
  }

  @Override
  protected ProcessResult process(byte[] input, Map<String, Object> options, JobId jobId)
      throws IOException {
    return doCompress(input);
  }

  /** Public for unit-test visibility. Runs qpdf --optimize-images on the input bytes. */
  public byte[] compress(byte[] input) throws IOException {
    PdfMagicValidator.validate("input", input);
    return doCompress(input).bytes();
  }

  private ProcessResult doCompress(byte[] input) throws IOException {
    Path workDir = Files.createTempDirectory("qpdf-compress-" + UUID.randomUUID());
    try {
      Path inFile = workDir.resolve("input.pdf");
      Path outFile = workDir.resolve("output.pdf");
      Files.write(inFile, input);
      List<String> cmd =
          List.of("qpdf", "--optimize-images", inFile.toAbsolutePath().toString(),
              outFile.toAbsolutePath().toString());
      Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
      boolean finished = p.waitFor(TIMEOUT_SEC, TimeUnit.SECONDS);
      if (!finished) {
        p.destroyForcibly();
        throw new IOException("qpdf timed out after " + TIMEOUT_SEC + "s");
      }
      if (p.exitValue() != 0) {
        String out = new String(p.getInputStream().readAllBytes());
        throw new IOException("qpdf exited " + p.exitValue() + ": " + out);
      }
      byte[] result = Files.readAllBytes(outFile);
      return new ProcessResult(result, "application/pdf", "compressed.pdf");
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      throw new IOException("Interrupted waiting for qpdf", ie);
    } finally {
      deleteRecursively(workDir);
    }
  }

  private static void deleteRecursively(Path root) {
    try (var stream = Files.walk(root)) {
      stream.sorted(Comparator.reverseOrder()).forEach(p -> {
        try {
          Files.deleteIfExists(p);
        } catch (IOException e) {
          LOG.warn("Failed to delete {}: {}", p, e.getMessage());
        }
      });
    } catch (IOException e) {
      LOG.warn("Failed to walk temp dir {}: {}", root, e.getMessage());
    }
  }
}
