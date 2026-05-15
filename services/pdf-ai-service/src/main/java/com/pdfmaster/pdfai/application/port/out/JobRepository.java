package com.pdfmaster.pdfai.application.port.out;

import com.pdfmaster.pdfai.domain.JobId;
import com.pdfmaster.pdfai.domain.JobRecord;
import com.pdfmaster.pdfai.domain.JobStatus;
import java.util.Optional;

public interface JobRepository {

  JobRecord save(JobRecord job);

  Optional<JobRecord> findById(JobId id);

  JobRecord markStatus(JobId id, JobStatus next);
}
