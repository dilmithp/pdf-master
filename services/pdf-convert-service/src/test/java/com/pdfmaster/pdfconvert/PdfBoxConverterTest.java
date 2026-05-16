package com.pdfmaster.pdfconvert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pdfmaster.pdfconvert.adapter.out.pdfbox.PdfBoxConverter;
import com.pdfmaster.pdfconvert.application.port.out.DocumentConverter.ConversionResult;
import com.pdfmaster.pdfconvert.domain.ConvertFormat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipInputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.Test;

class PdfBoxConverterTest {

  private final PdfBoxConverter converter = new PdfBoxConverter();

  @Test
  void pdfToPngProducesZip() throws Exception {
    byte[] pdf = singlePagePdf();
    ConversionResult result = converter.convert(pdf, ConvertFormat.PDF, ConvertFormat.PNG);

    assertThat(result.contentType()).isEqualTo("application/zip");
    int count = 0;
    try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(result.bytes()))) {
      while (zip.getNextEntry() != null) {
        count++;
      }
    }
    assertThat(count).isEqualTo(1);
  }

  @Test
  void pdfToJpgProducesZip() throws Exception {
    byte[] pdf = singlePagePdf();
    ConversionResult result = converter.convert(pdf, ConvertFormat.PDF, ConvertFormat.JPG);

    assertThat(result.contentType()).isEqualTo("application/zip");
  }

  @Test
  void pdfToTextExtractsContent() {
    byte[] pdf = singlePagePdf();
    ConversionResult result = converter.convert(pdf, ConvertFormat.PDF, ConvertFormat.TXT);

    assertThat(result.contentType()).contains("text/plain");
    assertThat(result.bytes()).isNotEmpty();
  }

  @Test
  void supportsReturnsTrueForImageOps() {
    assertThat(converter.supports(ConvertFormat.PDF, ConvertFormat.PNG)).isTrue();
    assertThat(converter.supports(ConvertFormat.PDF, ConvertFormat.JPG)).isTrue();
    assertThat(converter.supports(ConvertFormat.PDF, ConvertFormat.TXT)).isTrue();
  }

  @Test
  void supportsReturnsFalseForOfficeOps() {
    assertThat(converter.supports(ConvertFormat.PDF, ConvertFormat.DOCX)).isFalse();
    assertThat(converter.supports(ConvertFormat.DOCX, ConvertFormat.PDF)).isFalse();
  }

  @Test
  void rejectsUnsupportedPair() {
    assertThatThrownBy(() -> converter.convert(new byte[]{1}, ConvertFormat.DOCX, ConvertFormat.PDF))
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
