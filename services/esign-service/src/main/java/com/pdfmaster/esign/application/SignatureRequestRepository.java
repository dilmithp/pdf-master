package com.pdfmaster.esign.application;

import com.pdfmaster.esign.domain.SignatureRequest;
import java.util.Optional;
import java.util.UUID;

/** Outbound port for persisting signature requests. */
public interface SignatureRequestRepository {

  SignatureRequest save(SignatureRequest request);

  Optional<SignatureRequest> findById(UUID id);
}
