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
    ],
    sitemap: `${BRAND.baseUrl}/sitemap.xml`,
    host: BRAND.baseUrl,
  };
}
