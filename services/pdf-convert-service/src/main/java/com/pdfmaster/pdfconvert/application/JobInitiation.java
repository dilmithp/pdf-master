package com.pdfmaster.pdfconvert.application;

import com.pdfmaster.pdfconvert.domain.JobId;
import com.pdfmaster.pdfconvert.domain.PresignedUpload;
import java.util.List;
import java.util.Objects;

/** Carrier for the response to a job-creation request. */
public record JobInitiation(JobId jobId, List<PresignedUpload> uploadUrls) {

  public JobInitiation {
    Objects.requireNonNull(jobId, "jobId");
    Objects.requireNonNull(uploadUrls, "uploadUrls");
    uploadUrls = List.copyOf(uploadUrls);
  }
}
