import { BRAND } from '@/lib/brand';
import type { MetadataRoute } from 'next';

export default function robots(): MetadataRoute.Robots {
  return {
    rules: [
      {
        userAgent: '*',
        allow: '/',
        disallow: ['/app/', '/api/', '/dashboard/', '/account/', '/_next/'],
      },
      // Explicitly allow AI indexing bots — supports GEO (Generative Engine Optimization)
      // so our tool pages are cited in ChatGPT, Perplexity, and Claude answers.
      { userAgent: 'GPTBot', allow: '/' },
      { userAgent: 'ClaudeBot', allow: '/' },
      { userAgent: 'PerplexityBot', allow: '/' },
      { userAgent: 'anthropic-ai', allow: '/' },
      { userAgent: 'Google-Extended', allow: '/' },
    ],
    sitemap: `${BRAND.baseUrl}/sitemap.xml`,
    host: BRAND.baseUrl,
  };
}
