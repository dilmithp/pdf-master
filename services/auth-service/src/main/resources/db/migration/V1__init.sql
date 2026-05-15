-- auth-service initial schema.
-- Forward-only; rollback plan: DROP TABLE auth.users; (data destructive — replay from event log).

CREATE TABLE auth.users (
    id              UUID PRIMARY KEY,
    -- 320 = max RFC 5321 path length (64 local + @ + 255 domain).
    email           VARCHAR(320) NOT NULL,
    -- BCrypt hash is 60 chars; 100 leaves headroom for algorithm upgrades.
    password_hash   VARCHAR(100) NOT NULL,
    status          VARCHAR(32)  NOT NULL,
    created_at      TIMESTAMPTZ  NOT NULL,
    updated_at      TIMESTAMPTZ  NOT NULL
);

CREATE UNIQUE INDEX users_email_uidx ON auth.users (email);
CREATE INDEX users_status_idx ON auth.users (status);
