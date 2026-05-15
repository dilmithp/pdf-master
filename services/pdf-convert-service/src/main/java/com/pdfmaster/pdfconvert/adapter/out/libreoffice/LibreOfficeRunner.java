package com.pdfmaster.pdfconvert.adapter.out.libreoffice;

import com.pdfmaster.pdfconvert.application.port.out.DocumentConverter;
import com.pdfmaster.pdfconvert.config.LibreOfficeProperties;
import com.pdfmaster.pdfconvert.domain.ConvertFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adapter wrapping the {@code libreoffice --headless --convert-to ...} CLI. Each invocation runs in
 * an isolated temp directory with a dedicated user profile so concurrent jobs don't race.
 */
@Component
public class LibreOfficeRunner implements DocumentConverter {

  private static final Logger LOG = LoggerFactory.getLogger(LibreOfficeRunner.class);

  private final LibreOfficeProperties properties;

  public LibreOfficeRunner(LibreOfficeProperties properties) {
    this.properties = properties;
  }

  @Override
  public ConversionResult convert(byte[] input, ConvertFormat from, ConvertFormat to) {
    Path workDir = null;
    try {
      workDir = Files.createTempDirectory("loffice-" + UUID.randomUUID());
      Path source = workDir.resolve("source." + from.extension());
      Path profileDir = workDir.resolve("profile");
      Files.createDirectories(profileDir);
      Files.write(source, input);
      Path outDir = workDir.resolve("out");
      Files.createDirectories(outDir);
      List<String> cmd =
          List.of(
              properties.binary(),
              "-env:UserInstallation=file://" + profileDir.toAbsolutePath(),
              "--headless",
              "--nologo",
              "--nofirststartwizard",
              "--convert-to",
              to.extension(),
              "--outdir",
              outDir.toAbsolutePath().toString(),
              source.toAbsolutePath().toString());
      LOG.info("Running LibreOffice convert {} -> {}", from, to);
      Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
      boolean finished = p.waitFor(properties.timeoutSeconds(), TimeUnit.SECONDS);
      if (!finished) {
        p.destroyForcibly();
        throw new IOException(
            "LibreOffice convert timed out after " + properties.timeoutSeconds() + "s");
      }
      if (p.exitValue() != 0) {
        String stdout = new String(p.getInputStream().readAllBytes());
        throw new IOException(
            "LibreOffice exit "
                + p.exitValue()
                + " converting "
                + from
                + " -> "
                + to
                + ": "
                + stdout);
      }
      Path output = outDir.resolve("source." + to.extension());
      if (!Files.exists(output)) {
        throw new IOException("LibreOffice produced no output for " + from + " -> " + to);
      }
      return new ConversionResult(Files.readAllBytes(output), to.contentType());
    } catch (IOException ioe) {
      throw new IllegalStateException("LibreOffice conversion failed", ioe);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while waiting for LibreOffice", ie);
    } finally {
      if (workDir != null) {
        deleteRecursively(workDir);
      }
    }
  }

  private static void deleteRecursively(Path root) {
    try (var stream = Files.walk(root)) {
      stream
          .sorted(Comparator.reverseOrder())
          .forEach(
              p -> {
                try {
                  Files.deleteIfExists(p);
                } catch (IOException e) {
                  LOG.warn("Failed to delete temp file {}: {}", p, e.getMessage());
                }
              });
    } catch (IOException e) {
      LOG.warn("Failed to walk temp dir {}: {}", root, e.getMessage());
    }
  }
}
