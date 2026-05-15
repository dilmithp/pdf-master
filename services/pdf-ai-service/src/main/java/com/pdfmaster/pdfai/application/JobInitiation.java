package com.pdfmaster.pdfai.application;

import com.pdfmaster.pdfai.domain.JobId;
import com.pdfmaster.pdfai.domain.PresignedUpload;
import java.util.List;
import java.util.Objects;

public record JobInitiation(JobId jobId, List<PresignedUpload> uploadUrls) {

  public JobInitiation {
    Objects.requireNonNull(jobId);
    Objects.requireNonNull(uploadUrls);
    uploadUrls = List.copyOf(uploadUrls);
  }
}
