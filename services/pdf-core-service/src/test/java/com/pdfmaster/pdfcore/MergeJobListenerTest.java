package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pdfmaster.pdfcore.adapter.in.MergeJobListener;
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

class MergeJobListenerTest {

  @Test
  void mergesTwoSimplePdfs() throws Exception {
    byte[] a = singlePagePdf();
    byte[] b = singlePagePdf();

    ObjectStore store = mock(ObjectStore.class);
    when(store.download("a.pdf")).thenReturn(a);
    when(store.download("b.pdf")).thenReturn(b);

    MergeJobListener listener =
        new MergeJobListener(store, mock(JobRepository.class), Clock.systemUTC());

    byte[] merged = listener.mergeAll(List.of("a.pdf", "b.pdf"));

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(merged)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(2);
    }
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    ObjectStore store = mock(ObjectStore.class);
    when(store.download("bad.pdf")).thenReturn("not a pdf".getBytes());
    when(store.download("good.pdf")).thenReturn(singlePagePdf());

    MergeJobListener listener =
        new MergeJobListener(store, mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.mergeAll(List.of("bad.pdf", "good.pdf")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("magic-byte");
  }

  /** Build a tiny one-page PDF in memory — pure (no images / fonts) so tests run headlessly. */
  static byte[] singlePagePdf() {
    try (PDDocument doc = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      doc.addPage(new PDPage());
      doc.save(out);
      return out.toByteArray();
    } catch (Exception e) {
      throw new IllegalStateException("Failed to synthesise test PDF", e);
    }
  }
}
