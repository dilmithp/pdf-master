import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';

/**
 * All 27 EU member states + 3 EEA countries (Iceland, Liechtenstein, Norway).
 * ISO 3166-1 alpha-2 codes.
 */
export const EU_COUNTRY_CODES = new Set<string>([
  // EU member states
  'AT', 'BE', 'BG', 'CY', 'CZ', 'DE', 'DK', 'EE', 'ES', 'FI',
  'FR', 'GR', 'HR', 'HU', 'IE', 'IT', 'LT', 'LU', 'LV', 'MT',
  'NL', 'PL', 'PT', 'RO', 'SE', 'SI', 'SK',
  // EEA non-EU
  'IS', 'LI', 'NO',
]);

/**
 * Sets an X-Data-Region-Hint header to tell downstream backends
 * whether to route to eu-west-1 or us-east-1 based on request geo.
 *
 * Designed to be composed into a Clerk (or any other) middleware chain:
 *
 * ```ts
 * // middleware.ts
 * import { clerkMiddleware } from '@clerk/nextjs/server';
 * import { gdprMiddleware } from '@/lib/gdpr/middleware';
 *
 * export default clerkMiddleware((auth, req) => {
 *   const res = gdprMiddleware(req);
 *   return res ?? NextResponse.next();
 * });
 * ```
 */
export function gdprMiddleware(req: NextRequest): NextResponse {
  // Next.js 15 removed req.geo — read from edge-provided headers instead.
  // Cloudflare: cf-ipcountry; Vercel: x-vercel-ip-country.
  const country =
    req.headers.get('cf-ipcountry') ??
    req.headers.get('x-vercel-ip-country') ??
    'US';

  const isEU = EU_COUNTRY_CODES.has(country.toUpperCase());
  const dataRegion = isEU ? 'eu-west-1' : 'us-east-1';

  const res = NextResponse.next();
  res.headers.set('X-Data-Region-Hint', dataRegion);
  return res;
}
