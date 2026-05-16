-- Initial schema for pdf-core-service: audit log only.
-- Job state is held in-memory (InMemoryJobRepository); future migration will add a jobs table.
-- Rollback plan: DROP SCHEMA pdf_core CASCADE;

CREATE SCHEMA IF NOT EXISTS pdf_core;

CREATE TABLE pdf_core.audit_log (
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

CREATE INDEX idx_audit_log_user_id_created ON pdf_core.audit_log (user_id, created_at DESC);
CREATE INDEX idx_audit_log_action_created  ON pdf_core.audit_log (action, created_at DESC);
