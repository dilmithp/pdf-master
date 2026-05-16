package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.ReorderPagesJobListener;
import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Test;

class ReorderPagesJobListenerTest {

  @Test
  void reordersThreePagePdf() throws Exception {
    byte[] pdf = threePagePdf();
    ReorderPagesJobListener listener =
        new ReorderPagesJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.reorderPages(pdf, List.of(3, 1, 2));

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(3);
    }
  }

  @Test
  void rejectsOutOfBoundsPage() {
    byte[] pdf = threePagePdf();
    ReorderPagesJobListener listener =
        new ReorderPagesJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.reorderPages(pdf, List.of(1, 5)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("out of range");
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    ReorderPagesJobListener listener =
        new ReorderPagesJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.reorderPages("not a pdf".getBytes(), List.of(1)))
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
