import { describe, expect, it } from 'vitest';
import { BRAND } from '@/lib/brand';
import { GET } from './route';

describe('GET /security.txt', () => {
  it('returns text/plain content type', async () => {
    const res = GET();
    expect(res.headers.get('Content-Type')).toContain('text/plain');
  });

  it('contains RFC 9116 required Contact field', async () => {
    const res = GET();
    const body = await res.text();
    expect(body).toContain(`Contact: mailto:${BRAND.securityEmail}`);
  });

  it('contains Expires field with a future date', async () => {
    const res = GET();
    const body = await res.text();
    const match = /Expires: (.+)/.exec(body);
    expect(match).not.toBeNull();
    const expires = new Date(match![1]!);
    expect(expires.getTime()).toBeGreaterThan(Date.now());
  });

  it('contains Canonical field pointing to /.well-known/security.txt', async () => {
    const res = GET();
    const body = await res.text();
    expect(body).toContain(`Canonical: https://${BRAND.domain}/.well-known/security.txt`);
  });

  it('contains Policy field pointing to /legal/security', async () => {
    const res = GET();
    const body = await res.text();
    expect(body).toContain(`Policy: https://${BRAND.domain}/legal/security`);
  });

  it('contains Preferred-Languages field', async () => {
    const res = GET();
    const body = await res.text();
    expect(body).toContain('Preferred-Languages: en');
  });

  it('contains Encryption field', async () => {
    const res = GET();
    const body = await res.text();
    expect(body).toContain('Encryption:');
  });
});
