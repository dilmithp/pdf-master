package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.ExtractPagesJobListener;
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

class ExtractPagesJobListenerTest {

  @Test
  void extractsSinglePageFromThreePage() throws Exception {
    byte[] pdf = threePagePdf();
    ExtractPagesJobListener listener =
        new ExtractPagesJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.extractPages(pdf, "2");

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(1);
    }
  }

  @Test
  void extractsRangeFromThreePage() throws Exception {
    byte[] pdf = threePagePdf();
    ExtractPagesJobListener listener =
        new ExtractPagesJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.extractPages(pdf, "1-2");

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(2);
    }
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    ExtractPagesJobListener listener =
        new ExtractPagesJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.extractPages("not a pdf".getBytes(), "1"))
        .isInstanceOf(Exception.class);
  }

  static byte[] threePagePdf() {
    try (PDDocument doc = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
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
