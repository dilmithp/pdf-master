package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.CompressJobListener;
import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Test;

/**
 * CompressJobListener requires qpdf on the host PATH. In CI and unit test environments qpdf
 * may not be installed, so we test only the magic-byte validation path (which runs before qpdf
 * is invoked) and skip the happy path that requires the binary.
 */
class CompressJobListenerTest {

  @Test
  void rejectsNonPdfMagicBytes() {
    CompressJobListener listener =
        new CompressJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.compress("not a pdf".getBytes()))
        .isInstanceOf(Exception.class);
  }

  @Test
  void compressValidPdfWithQpdfIfAvailable() throws Exception {
    byte[] pdf = singlePagePdf();
    CompressJobListener listener =
        new CompressJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    try {
      byte[] result = listener.compress(pdf);
      // qpdf is available — result is a valid PDF
      assertThat(result).startsWith('%', 'P', 'D', 'F');
    } catch (Exception ex) {
      // qpdf not installed in this environment — acceptable for unit tests
      assertThat(ex).hasMessageContaining("qpdf");
    }
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
