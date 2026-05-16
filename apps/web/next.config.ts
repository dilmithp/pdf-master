import { withSentryConfig } from '@sentry/nextjs';
import type { NextConfig } from 'next';

const config: NextConfig = {
  reactStrictMode: true,
  poweredByHeader: false,
  experimental: {
    typedRoutes: true,
  },
  images: {
    formats: ['image/avif', 'image/webp'],
  },
  async headers() {
    const securityHeaders = [
      { key: 'X-Content-Type-Options', value: 'nosniff' },
      { key: 'X-Frame-Options', value: 'DENY' },
      { key: 'Referrer-Policy', value: 'strict-origin-when-cross-origin' },
      { key: 'Permissions-Policy', value: 'camera=(), microphone=(), geolocation=()' },
      {
        key: 'Strict-Transport-Security',
        value: 'max-age=63072000; includeSubDomains; preload',
      },
    ];
    return [{ source: '/:path*', headers: securityHeaders }];
  },
};

const sentryOrg = process.env.SENTRY_ORG;
const sentryProject = process.env.SENTRY_PROJECT;
const sentryAuthToken = process.env.SENTRY_AUTH_TOKEN;

export default withSentryConfig(config, {
  // Only upload source maps when SENTRY_AUTH_TOKEN is present (CI/CD only)
  silent: !sentryAuthToken,
  ...(sentryOrg !== undefined ? { org: sentryOrg } : {}),
  ...(sentryProject !== undefined ? { project: sentryProject } : {}),
  ...(sentryAuthToken !== undefined ? { authToken: sentryAuthToken } : {}),

  // Disable automatic instrumentation of server-side pages
  // We manage Sentry init explicitly via instrumentation.ts
  autoInstrumentServerFunctions: false,

  // Tunnel Sentry requests through own domain to avoid ad-blockers
  tunnelRoute: '/monitoring-tunnel',

  // Tree-shake Sentry debug code in production
  disableLogger: true,

  // Do not hide source maps from the browser (they are already on CDN)
  hideSourceMaps: false,
});
