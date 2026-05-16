package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.PageNumbersJobListener;
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

class PageNumbersJobListenerTest {

  @Test
  void pageNumbersProducesValidPdfWithSamePageCount() throws Exception {
    byte[] pdf = twoPagePdf();
    PageNumbersJobListener listener =
        new PageNumbersJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.addPageNumbers(pdf);

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(2);
    }
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    PageNumbersJobListener listener =
        new PageNumbersJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.addPageNumbers("not a pdf".getBytes()))
        .isInstanceOf(Exception.class);
  }

  static byte[] twoPagePdf() {
    try (PDDocument doc = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      doc.addPage(new PDPage());
      doc.addPage(new PDPage());
      doc.save(out);
      return out.toByteArray();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
