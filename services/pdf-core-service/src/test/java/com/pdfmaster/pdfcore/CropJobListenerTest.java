package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.CropJobListener;
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

class CropJobListenerTest {

  @Test
  void cropWithMarginsProducesValidPdf() throws Exception {
    byte[] pdf = singlePagePdf();
    CropJobListener listener =
        new CropJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.crop(pdf, 10f, 10f, 10f, 10f);

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getNumberOfPages()).isEqualTo(1);
      // CropBox should be smaller than the default A4 MediaBox
      float cropWidth = doc.getPage(0).getCropBox().getWidth();
      assertThat(cropWidth).isLessThan(doc.getPage(0).getMediaBox().getWidth());
    }
  }

  @Test
  void rejectsMarginsLargerThanPage() {
    byte[] pdf = singlePagePdf();
    CropJobListener listener =
        new CropJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    // A4 is ~595 x 842 points; margins of 400 each side exceed the width
    assertThatThrownBy(() -> listener.crop(pdf, 400f, 400f, 400f, 400f))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Crop margins exceed page dimensions");
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    CropJobListener listener =
        new CropJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.crop("not a pdf".getBytes(), 0f, 0f, 0f, 0f))
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
