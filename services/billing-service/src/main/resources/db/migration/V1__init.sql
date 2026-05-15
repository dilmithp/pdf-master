-- billing-service initial schema.
-- Forward-only; rollback plan: DROP TABLE billing.subscriptions; subscription state can be
-- rebuilt from Stripe by replaying customer.subscription.* webhooks.

CREATE TABLE billing.subscriptions (
    id                      UUID PRIMARY KEY,
    user_id                 UUID         NOT NULL,
    -- Stripe IDs are at most 255 chars but typically <40; 64 is generous and indexable.
    stripe_subscription_id  VARCHAR(64)  NOT NULL,
    plan                    VARCHAR(32)  NOT NULL,
    status                  VARCHAR(32)  NOT NULL,
    current_period_end      TIMESTAMPTZ  NOT NULL
);

CREATE UNIQUE INDEX subscriptions_user_id_uidx ON billing.subscriptions (user_id);
CREATE UNIQUE INDEX subscriptions_stripe_id_uidx ON billing.subscriptions (stripe_subscription_id);
CREATE INDEX subscriptions_status_idx ON billing.subscriptions (status);
