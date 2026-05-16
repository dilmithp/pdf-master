-- Idempotency ledger for incoming Stripe webhook events.
-- Uses the Stripe event id (evt_*) as the PK so duplicate deliveries are detected via PK collision.
-- Rollback plan: DROP TABLE billing.stripe_event; idempotency guard is lost — replay protection
-- must be re-enabled before processing live webhooks again.

CREATE TABLE billing.stripe_event (
    id            VARCHAR(128) PRIMARY KEY,    -- Stripe event id (evt_*)
    event_type    VARCHAR(128) NOT NULL,
    payload       JSONB        NOT NULL,
    received_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_stripe_event_type_received ON billing.stripe_event (event_type, received_at DESC);
