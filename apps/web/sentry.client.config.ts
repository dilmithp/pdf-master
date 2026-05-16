import * as Sentry from '@sentry/nextjs';

const dsn = process.env.NEXT_PUBLIC_SENTRY_DSN;

Sentry.init({
  ...(dsn !== undefined ? { dsn } : {}),
  enabled: !!dsn,

  // Capture 10% of transactions for performance monitoring
  tracesSampleRate: 0.1,

  // Do not record sessions during normal browsing (privacy-first)
  replaysSessionSampleRate: 0.0,

  // Record full session replay only on errors
  replaysOnErrorSampleRate: 1.0,

  integrations: [
    Sentry.replayIntegration({
      // Mask all text and block all media by default (GDPR compliance)
      maskAllText: true,
      blockAllMedia: true,
    }),
  ],
});
