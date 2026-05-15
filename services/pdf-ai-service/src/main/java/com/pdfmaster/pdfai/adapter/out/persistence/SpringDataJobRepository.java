package com.pdfmaster.pdfai.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJobRepository extends JpaRepository<JobEntity, UUID> {}
