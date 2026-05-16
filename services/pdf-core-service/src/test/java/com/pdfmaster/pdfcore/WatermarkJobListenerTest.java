package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.WatermarkJobListener;
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

class WatermarkJobListenerTest {

  @Test
  void watermarkProducesValidPdf() throws Exception {
    byte[] pdf = singlePagePdf();
    WatermarkJobListener listener =
        new WatermarkJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.watermark(pdf, "DRAFT");

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(1);
    }
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    WatermarkJobListener listener =
        new WatermarkJobListener(
            mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.watermark("not a pdf".getBytes(), "DRAFT"))
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
