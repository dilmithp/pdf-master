package com.pdfmaster.esign.adapter.out.persistence;

import com.pdfmaster.esign.application.SignatureRequestRepository;
import com.pdfmaster.esign.domain.SignatureRequest;
import com.pdfmaster.esign.domain.Signer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Adapts the JPA repository to the application-layer port. */
@Repository
public class SignatureRequestRepositoryAdapter implements SignatureRequestRepository {

  private final SignatureRequestJpaRepository jpa;

  public SignatureRequestRepositoryAdapter(SignatureRequestJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public SignatureRequest save(SignatureRequest request) {
    List<SignatureRequestEntity.SignerData> signers =
        request.signers().stream()
            .map(s -> new SignatureRequestEntity.SignerData(s.email(), s.order(), s.signedAt()))
            .toList();
    SignatureRequestEntity entity =
        new SignatureRequestEntity(
            request.id(),
            request.senderId(),
            request.documentS3Key(),
            request.status(),
            signers,
            request.createdAt());
    return toDomain(jpa.save(entity));
  }

  @Override
  public Optional<SignatureRequest> findById(UUID id) {
    return jpa.findById(id).map(SignatureRequestRepositoryAdapter::toDomain);
  }

  private static SignatureRequest toDomain(SignatureRequestEntity e) {
    List<Signer> signers =
        e.getSigners().stream().map(s -> new Signer(s.email(), s.order(), s.signedAt())).toList();
    return new SignatureRequest(
        e.getId(), e.getSenderId(), e.getDocumentS3Key(), e.getStatus(), signers, e.getCreatedAt());
  }
}
