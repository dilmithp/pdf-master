package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.RotateJobListener;
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

class RotateJobListenerTest {

  @Test
  void rotatesAllPages90Degrees() throws Exception {
    byte[] pdf = singlePagePdf();
    RotateJobListener listener =
        new RotateJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.rotate(pdf, 90, "all");

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getPage(0).getRotation()).isEqualTo(90);
    }
  }

  @Test
  void rotates180Degrees() throws Exception {
    byte[] pdf = singlePagePdf();
    RotateJobListener listener =
        new RotateJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] result = listener.rotate(pdf, 180, "all");

    try (PDDocument doc =
        Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(result)))) {
      assertThat(doc.getPage(0).getRotation()).isEqualTo(180);
    }
  }

  @Test
  void rejectsInvalidDegrees() {
    RotateJobListener listener =
        new RotateJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.rotate(singlePagePdf(), 45, "all"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("degrees");
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    RotateJobListener listener =
        new RotateJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.rotate("not a pdf".getBytes(), 90, "all"))
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
