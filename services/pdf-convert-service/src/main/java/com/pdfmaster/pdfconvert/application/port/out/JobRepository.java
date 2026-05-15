package com.pdfmaster.pdfconvert.application.port.out;

import com.pdfmaster.pdfconvert.domain.JobId;
import com.pdfmaster.pdfconvert.domain.JobRecord;
import com.pdfmaster.pdfconvert.domain.JobStatus;
import java.util.Optional;

/** Outbound port for job state persistence. */
public interface JobRepository {

  JobRecord save(JobRecord job);

  Optional<JobRecord> findById(JobId id);

  JobRecord markStatus(JobId id, JobStatus next);
}
