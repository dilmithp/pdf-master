import { BRAND } from '@/lib/brand';

export const dynamic = 'force-static';

export function GET(): Response {
  const expires = new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toISOString();
  const body = [
    `Contact: mailto:${BRAND.securityEmail}`,
    `Expires: ${expires}`,
    `Encryption: https://${BRAND.domain}/.well-known/pgp-key.txt`,
    `Preferred-Languages: en`,
    `Canonical: https://${BRAND.domain}/.well-known/security.txt`,
    `Policy: https://${BRAND.domain}/legal/security`,
  ].join('\n');

  return new Response(body, {
    headers: { 'Content-Type': 'text/plain; charset=utf-8' },
  });
}
