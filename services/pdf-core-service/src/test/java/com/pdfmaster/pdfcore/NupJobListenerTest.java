package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.NupJobListener;
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

class NupJobListenerTest {

  @Test
  void twoUpFourPagesProducesTwoSheets() throws Exception {
    byte[] pdf = fourPagePdf();
    NupJobListener listener =
        new NupJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.nup(pdf, 2);

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(2);
    }
  }

  @Test
  void fourUpFourPagesProducesOneSheet() throws Exception {
    byte[] pdf = fourPagePdf();
    NupJobListener listener =
        new NupJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.nup(pdf, 4);

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(1);
    }
  }

  @Test
  void rejectsInvalidNup() {
    NupJobListener listener =
        new NupJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.nup(fourPagePdf(), 3))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("nup must be 2 or 4");
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    NupJobListener listener =
        new NupJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.nup("not a pdf".getBytes(), 2))
        .isInstanceOf(Exception.class);
  }

  static byte[] fourPagePdf() {
    try (PDDocument doc = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      doc.addPage(new PDPage());
      doc.addPage(new PDPage());
      doc.addPage(new PDPage());
      doc.addPage(new PDPage());
      doc.save(out);
      return out.toByteArray();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
