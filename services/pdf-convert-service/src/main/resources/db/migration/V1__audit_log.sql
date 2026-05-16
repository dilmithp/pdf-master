-- Initial schema for pdf-convert-service: audit log only.
-- Rollback plan: DROP SCHEMA pdf_convert CASCADE;

CREATE SCHEMA IF NOT EXISTS pdf_convert;

CREATE TABLE pdf_convert.audit_log (
    id            UUID PRIMARY KEY,
    user_id       VARCHAR(255),
    action        VARCHAR(64)  NOT NULL,
    resource      VARCHAR(512),
    ip            VARCHAR(64),
    user_agent    VARCHAR(512),
    status_code   SMALLINT,
    error         TEXT,
    metadata      JSONB,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_log_user_id_created ON pdf_convert.audit_log (user_id, created_at DESC);
CREATE INDEX idx_audit_log_action_created  ON pdf_convert.audit_log (action, created_at DESC);
