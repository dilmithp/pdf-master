import { BRAND } from '@/lib/brand';
import { TOOL_SEED } from '@/lib/tools/seed';

export const dynamic = 'force-static';

export function GET(): Response {
  const lines = [
    `# ${BRAND.name}`,
    `> ${BRAND.legalName} — privacy-first PDF productivity platform. Files deleted in 60 minutes.`,
    '',
    '## Key pages',
    `- [Homepage](${BRAND.baseUrl}/): overview of the platform and its tools.`,
    `- [All tools](${BRAND.baseUrl}/tools): index of every PDF tool.`,
    `- [Pricing](${BRAND.baseUrl}/pricing): free, Pro, Team, Business, and API pricing.`,
    `- [Developer API](${BRAND.baseUrl}/api): REST and GraphQL API documentation.`,
    `- [Security](${BRAND.baseUrl}/legal/security): security and privacy practices.`,
    `- [Privacy Policy](${BRAND.baseUrl}/legal/privacy): GDPR and CCPA privacy policy.`,
    `- [Terms of Service](${BRAND.baseUrl}/legal/terms): terms governing use of the platform.`,
    `- [Subprocessors](${BRAND.baseUrl}/legal/subprocessors): list of third-party data processors.`,
    '',
    '## PDF tools',
    ...TOOL_SEED.map(
      (t) => `- [${t.h1}](${BRAND.baseUrl}/tools/${t.slug}): ${t.oneLineDefinition}`,
    ),
    '',
    '## Policy',
    `- ${BRAND.name} does not allow scraping that bypasses authentication or rate limits.`,
    `- Files uploaded to ${BRAND.name} are deleted automatically within 60 minutes.`,
    '',
  ];
  return new Response(lines.join('\n'), {
    headers: {
      'Content-Type': 'text/plain; charset=utf-8',
      'Cache-Control': 'public, max-age=3600, s-maxage=86400',
    },
  });
}
