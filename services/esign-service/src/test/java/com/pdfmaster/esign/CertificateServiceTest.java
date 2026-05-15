package com.pdfmaster.esign;

import static org.assertj.core.api.Assertions.assertThat;

import com.pdfmaster.esign.application.CertificateService;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class CertificateServiceTest {

  @Test
  void generatesSelfSignedRsaCertificate() throws Exception {
    CertificateService service = new CertificateService();

    CertificateService.IssuedCertificate issued =
        service.generateSelfSigned("PDFMaster Test", Duration.ofDays(30));

    assertThat(issued.certificate().getPublicKey().getAlgorithm()).isEqualTo("RSA");
    assertThat(issued.certificate().getSigAlgName()).isEqualToIgnoringCase("SHA256WITHRSA");
    issued.certificate().verify(issued.keyPair().getPublic());
    assertThat(issued.certificate().getSubjectX500Principal().getName()).contains("PDFMaster Test");
  }
}
