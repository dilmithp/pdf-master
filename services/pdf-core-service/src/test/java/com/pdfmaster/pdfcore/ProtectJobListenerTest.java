package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.ProtectJobListener;
import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Test;

class ProtectJobListenerTest {

  @Test
  void protectProducesEncryptedPdf() throws Exception {
    byte[] pdf = singlePagePdf();
    ProtectJobListener listener =
        new ProtectJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.protect(pdf, "s3cret");

    // Verify it's a PDF (magic bytes)
    assertThat(result).startsWith('%', 'P', 'D', 'F');
    // Verify password is required to open it
    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)), "s3cret")) {
      assertThat(doc.getNumberOfPages()).isEqualTo(1);
    }
  }

  @Test
  void rejectsBlankPassword() {
    ProtectJobListener listener =
        new ProtectJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.protect(singlePagePdf(), ""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("userPassword");
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    ProtectJobListener listener =
        new ProtectJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.protect("not a pdf".getBytes(), "pwd"))
        .isInstanceOf(Exception.class);
  }

  static byte[] singlePagePdf() {
    try (PDDocument doc = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      doc.addPage(new PDPage());
      doc.save(out);
      return out.toByteArray();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
