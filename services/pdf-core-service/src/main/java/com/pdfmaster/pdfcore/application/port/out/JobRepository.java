package com.pdfmaster.pdfcore.application.port.out;

import com.pdfmaster.pdfcore.domain.JobId;
import com.pdfmaster.pdfcore.domain.JobRecord;
import com.pdfmaster.pdfcore.domain.JobStatus;
import java.util.Optional;

/** Outbound port for job state persistence. */
public interface JobRepository {

  JobRecord save(JobRecord job);

  Optional<JobRecord> findById(JobId id);

  JobRecord markStatus(JobId id, JobStatus next);
}
