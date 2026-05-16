import { describe, expect, it, vi } from 'vitest';

// Mock next/server before importing the module under test.
// vitest runs in jsdom which has no Next.js runtime, so we stub NextResponse.
vi.mock('next/server', () => ({
  NextResponse: {
    next: () => {
      const store = new Map<string, string>();
      return {
        headers: {
          set: (key: string, value: string) => store.set(key, value),
          get: (key: string): string | null => store.get(key) ?? null,
        },
      };
    },
  },
}));

import { EU_COUNTRY_CODES, gdprMiddleware } from './middleware';

function makeNextRequest(
  countryCode: string,
  useGeo = true,
  useCloudflare = false,
) {
  const cfHeaders: Record<string, string> = {};
  if (useCloudflare) {
    cfHeaders['cf-ipcountry'] = countryCode;
  }

  return {
    geo: useGeo ? { country: countryCode } : undefined,
    headers: {
      get: (key: string): string | null => cfHeaders[key] ?? null,
    },
  } as unknown as import('next/server').NextRequest;
}

describe('EU_COUNTRY_CODES', () => {
  it('contains all 27 EU member states', () => {
    const eu27 = [
      'AT', 'BE', 'BG', 'CY', 'CZ', 'DE', 'DK', 'EE', 'ES', 'FI',
      'FR', 'GR', 'HR', 'HU', 'IE', 'IT', 'LT', 'LU', 'LV', 'MT',
      'NL', 'PL', 'PT', 'RO', 'SE', 'SI', 'SK',
    ];
    for (const code of eu27) {
      expect(EU_COUNTRY_CODES.has(code), `Missing EU country: ${code}`).toBe(true);
    }
  });

  it('contains all 3 EEA non-EU countries', () => {
    expect(EU_COUNTRY_CODES.has('IS')).toBe(true);
    expect(EU_COUNTRY_CODES.has('LI')).toBe(true);
    expect(EU_COUNTRY_CODES.has('NO')).toBe(true);
  });

  it('does not contain US', () => {
    expect(EU_COUNTRY_CODES.has('US')).toBe(false);
  });
});

describe('gdprMiddleware', () => {
  it('sets eu-west-1 for EU country (Germany) via geo', () => {
    const req = makeNextRequest('DE', true, false);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('eu-west-1');
  });

  it('sets eu-west-1 for France via geo', () => {
    const req = makeNextRequest('FR', true, false);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('eu-west-1');
  });

  it('sets us-east-1 for US via geo', () => {
    const req = makeNextRequest('US', true, false);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('us-east-1');
  });

  it('falls back to cf-ipcountry header when geo is absent', () => {
    const req = makeNextRequest('DE', false, true);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('eu-west-1');
  });

  it('defaults to us-east-1 when no geo and no cf-ipcountry', () => {
    const req = makeNextRequest('US', false, false);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('us-east-1');
  });

  it('handles lowercase country codes (case-insensitive)', () => {
    const req = makeNextRequest('de', true, false);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('eu-west-1');
  });

  it('sets eu-west-1 for EEA country Norway', () => {
    const req = makeNextRequest('NO', true, false);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('eu-west-1');
  });

  it('sets us-east-1 for GB (post-Brexit, not in EU/EEA)', () => {
    const req = makeNextRequest('GB', true, false);
    const res = gdprMiddleware(req);
    expect(res.headers.get('X-Data-Region-Hint')).toBe('us-east-1');
  });
});
