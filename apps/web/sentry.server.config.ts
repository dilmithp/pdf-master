import * as Sentry from '@sentry/nextjs';

const dsn = process.env.NEXT_PUBLIC_SENTRY_DSN;

Sentry.init({
  ...(dsn !== undefined ? { dsn } : {}),
  enabled: !!dsn,

  // Capture 10% of transactions for performance monitoring
  tracesSampleRate: 0.1,
});
