package com.pdfmaster.pdfai.application.port.out;

import com.pdfmaster.pdfai.domain.DocumentChunk;
import com.pdfmaster.pdfai.domain.JobId;
import java.util.List;

/** Outbound port for persisting pgvector-backed document chunks. */
public interface DocumentChunkRepository {

  DocumentChunk save(DocumentChunk chunk);

  List<DocumentChunk> findByJobId(JobId jobId);
}
