-- notification-service initial schema.
-- Forward-only; rollback plan: DROP TABLE notification.notification_templates;
-- (templates are version-controlled; reload from source on roll-forward).

CREATE TABLE notification.notification_templates (
    id                UUID PRIMARY KEY,
    -- Stable template code referenced by other services (e.g. "user.welcome").
    code              VARCHAR(128) NOT NULL,
    -- BCP-47 locale tag; longest BCP-47 tags peak ~16 chars (e.g. "zh-Hant-CN-x-y").
    locale            VARCHAR(16)  NOT NULL,
    subject_template  VARCHAR(512) NOT NULL,
    body_template     TEXT         NOT NULL,
    CONSTRAINT templates_code_locale_uk UNIQUE (code, locale)
);

CREATE INDEX notification_templates_code_idx ON notification.notification_templates (code);
