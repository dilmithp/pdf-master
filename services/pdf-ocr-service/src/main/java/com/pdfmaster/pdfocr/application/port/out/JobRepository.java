package com.pdfmaster.pdfocr.application.port.out;

import com.pdfmaster.pdfocr.domain.JobId;
import com.pdfmaster.pdfocr.domain.JobRecord;
import com.pdfmaster.pdfocr.domain.JobStatus;
import java.util.Optional;

public interface JobRepository {

  JobRecord save(JobRecord job);

  Optional<JobRecord> findById(JobId id);

  JobRecord markStatus(JobId id, JobStatus next);
}
