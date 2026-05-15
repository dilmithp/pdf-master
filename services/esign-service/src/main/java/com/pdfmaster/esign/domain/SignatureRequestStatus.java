package com.pdfmaster.esign.domain;

/** Lifecycle status for a {@link SignatureRequest}. */
public enum SignatureRequestStatus {
  DRAFT,
  SENT,
  PARTIALLY_SIGNED,
  COMPLETED,
  DECLINED,
  EXPIRED,
  VOIDED
}
