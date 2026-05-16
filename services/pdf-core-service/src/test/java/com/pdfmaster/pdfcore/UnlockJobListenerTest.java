package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.UnlockJobListener;
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

class UnlockJobListenerTest {

  @Test
  void unlockUnencryptedPdfReturnsSameContent() throws Exception {
    byte[] pdf = singlePagePdf();
    UnlockJobListener listener =
        new UnlockJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.unlock(pdf, "");

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(1);
      assertThat(doc.isEncrypted()).isFalse();
    }
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    UnlockJobListener listener =
        new UnlockJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.unlock("not a pdf".getBytes(), ""))
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
