package com.pdfmaster.pdfcore.domain;

/** Coarse-grained job lifecycle states surfaced to the API. */
public enum JobStatus {
  QUEUED,
  RUNNING,
  SUCCEEDED,
  FAILED
}
