-- Initial schema for pdf-ai-service.
-- Creates the pgvector extension (must exist on every Postgres instance hosting the schema) and
-- the two operational tables: job (worker job state) and document_chunk (embedded text chunks).

CREATE EXTENSION IF NOT EXISTS vector;

CREATE SCHEMA IF NOT EXISTS pdf_ai;

CREATE TABLE pdf_ai.job (
    id              UUID PRIMARY KEY,
    op              VARCHAR(64)  NOT NULL,
    status          VARCHAR(16)  NOT NULL,
    input_keys      TEXT         NOT NULL,
    output_key      VARCHAR(512),
    error_message   VARCHAR(2000),
    created_at      TIMESTAMPTZ  NOT NULL,
    updated_at      TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_job_status     ON pdf_ai.job (status);
CREATE INDEX idx_job_created_at ON pdf_ai.job (created_at);

CREATE TABLE pdf_ai.document_chunk (
    id        UUID PRIMARY KEY,
    job_id    UUID         NOT NULL,
    content   TEXT         NOT NULL,
    embedding vector(1536) NOT NULL
);

CREATE INDEX idx_document_chunk_job_id ON pdf_ai.document_chunk (job_id);
