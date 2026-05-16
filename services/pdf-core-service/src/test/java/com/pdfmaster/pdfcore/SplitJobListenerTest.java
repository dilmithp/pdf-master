package com.pdfmaster.pdfcore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.pdfmaster.pdfcore.adapter.in.SplitJobListener;
import com.pdfmaster.pdfcore.application.port.out.JobRepository;
import com.pdfmaster.pdfcore.application.port.out.ObjectStore;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Clock;
import java.util.zip.ZipInputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Test;

class SplitJobListenerTest {

  @Test
  void splitsTwoPagePdfIntoTwoZipEntries() throws Exception {
    byte[] pdf = twoPagePdf();
    SplitJobListener listener =
        new SplitJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] zip = listener.splitToZip(pdf, null);

    int count = 0;
    try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip))) {
      while (zis.getNextEntry() != null) {
        count++;
      }
    }
    assertThat(count).isEqualTo(2);
  }

  @Test
  void splitWithRangeProducesOneEntry() throws Exception {
    byte[] pdf = twoPagePdf();
    SplitJobListener listener =
        new SplitJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    byte[] zip = listener.splitToZip(pdf, "1-2");

    int count = 0;
    try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip))) {
      while (zis.getNextEntry() != null) {
        count++;
      }
    }
    assertThat(count).isEqualTo(1);
  }

  @Test
  void rejectsNonPdfMagicBytes() {
    SplitJobListener listener =
        new SplitJobListener(mock(ObjectStore.class), mock(JobRepository.class), Clock.systemUTC());

    assertThatThrownBy(() -> listener.splitToZip("not a pdf".getBytes(), null))
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
