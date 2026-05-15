package com.pdfmaster.esign.application;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

/**
 * Scaffolding utility that exercises the BouncyCastle dependency by generating a self-signed
 * X.509v3 certificate. Not yet wired into a controller — production e-signature certificate
 * issuance will go through a CA integration in a follow-up.
 */
@Service
public class CertificateService {

  static {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  /** Generates a 2048-bit RSA self-signed certificate valid for the supplied duration. */
  public IssuedCertificate generateSelfSigned(String subjectCommonName, Duration validity)
      throws GeneralSecurityException, OperatorCreationException, IOException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    KeyPair keyPair = generator.generateKeyPair();

    Instant now = Instant.now();
    X500Name dn = new X500Name("CN=" + subjectCommonName);
    BigInteger serial = BigInteger.valueOf(now.toEpochMilli());

    X509v3CertificateBuilder builder =
        new JcaX509v3CertificateBuilder(
            dn, serial, Date.from(now), Date.from(now.plus(validity)), dn, keyPair.getPublic());

    ContentSigner signer =
        new JcaContentSignerBuilder("SHA256WithRSA")
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .build(keyPair.getPrivate());

    X509Certificate certificate =
        new JcaX509CertificateConverter()
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .getCertificate(builder.build(signer));

    return new IssuedCertificate(certificate, keyPair);
  }

  /** Holder returned from certificate generation. */
  public record IssuedCertificate(X509Certificate certificate, KeyPair keyPair) {}
}
