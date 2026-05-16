package com.pdfmaster.esign.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link SignatureRequestEntity}. */
public interface SignatureRequestJpaRepository
    extends JpaRepository<SignatureRequestEntity, UUID> {

  long deleteBySenderId(UUID senderId);
}
