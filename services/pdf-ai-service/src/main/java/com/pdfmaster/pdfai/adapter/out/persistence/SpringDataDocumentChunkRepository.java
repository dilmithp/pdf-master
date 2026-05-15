package com.pdfmaster.pdfai.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDocumentChunkRepository
    extends JpaRepository<DocumentChunkEntity, UUID> {

  List<DocumentChunkEntity> findByJobId(UUID jobId);
}
