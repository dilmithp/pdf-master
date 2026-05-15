package com.pdfmaster.esign.application;

import com.pdfmaster.esign.domain.SignatureRequest;
import com.pdfmaster.esign.domain.SignatureRequestStatus;
import com.pdfmaster.esign.domain.Signer;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use cases for signature request lifecycle. */
@Service
public class SignatureRequestService {

  private final SignatureRequestRepository repository;
  private final Clock clock;

  public SignatureRequestService(SignatureRequestRepository repository, Clock clock) {
    this.repository = repository;
    this.clock = clock;
  }

  @Transactional
  public SignatureRequest create(UUID senderId, String documentS3Key, List<Signer> signers) {
    SignatureRequest request =
        new SignatureRequest(
            UUID.randomUUID(),
            senderId,
            documentS3Key,
            SignatureRequestStatus.DRAFT,
            signers,
            clock.instant());
    return repository.save(request);
  }

  @Transactional(readOnly = true)
  public Optional<SignatureRequest> findById(UUID id) {
    return repository.findById(id);
  }
}
