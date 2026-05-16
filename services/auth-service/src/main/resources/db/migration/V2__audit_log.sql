-- GDPR audit log for auth-service.
-- Rollback plan: DROP TABLE auth.audit_log; (data loss acceptable — audit trail is not operational data).

CREATE TABLE auth.audit_log (
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

CREATE INDEX idx_audit_log_user_id_created ON auth.audit_log (user_id, created_at DESC);
CREATE INDEX idx_audit_log_action_created  ON auth.audit_log (action, created_at DESC);
