-- esign-service initial schema.
-- Forward-only; rollback plan: DROP TABLE esign.signature_requests;
-- (Signature requests are append-driven and can be re-issued; document objects in S3 are
-- independent of this table.)

CREATE TABLE esign.signature_requests (
    id                UUID PRIMARY KEY,
    sender_id         UUID         NOT NULL,
    -- S3 keys up to 1024 bytes per AWS spec; 512 covers our envelope-id/document.pdf pattern.
    document_s3_key   VARCHAR(512) NOT NULL,
    status            VARCHAR(32)  NOT NULL,
    -- Ordered signer list with signed_at timestamps. JSONB allows future schema evolution
    -- without a migration; ordering is preserved by array index.
    signers           JSONB        NOT NULL DEFAULT '[]'::jsonb,
    created_at        TIMESTAMPTZ  NOT NULL
);

CREATE INDEX signature_requests_sender_id_idx ON esign.signature_requests (sender_id);
CREATE INDEX signature_requests_status_idx ON esign.signature_requests (status);
CREATE INDEX signature_requests_created_at_idx ON esign.signature_requests (created_at DESC);
