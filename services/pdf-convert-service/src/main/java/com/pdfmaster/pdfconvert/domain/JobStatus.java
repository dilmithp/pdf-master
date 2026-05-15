package com.pdfmaster.pdfconvert.domain;

/** Coarse-grained job lifecycle states surfaced to the API. */
public enum JobStatus {
  QUEUED,
  RUNNING,
  SUCCEEDED,
  FAILED
}
