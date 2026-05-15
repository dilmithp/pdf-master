package com.pdfmaster.billing.domain;

/** Pricing tiers. Plan-to-Stripe-price mapping is held by the billing service config. */
public enum Plan {
  FREE,
  PRO,
  TEAM,
  BUSINESS
}
