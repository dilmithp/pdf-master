import { BRAND } from '@/lib/brand';
import { TOOL_SEED } from '@/lib/tools/seed';
import type { MetadataRoute } from 'next';

const STATIC_PATHS: ReadonlyArray<{
  path: string;
  priority: number;
  changeFrequency: MetadataRoute.Sitemap[number]['changeFrequency'];
}> = [
  { path: '/', priority: 1.0, changeFrequency: 'weekly' },
  { path: '/tools', priority: 0.9, changeFrequency: 'weekly' },
  { path: '/pricing', priority: 0.8, changeFrequency: 'monthly' },
  { path: '/api', priority: 0.7, changeFrequency: 'monthly' },
  { path: '/security', priority: 0.6, changeFrequency: 'monthly' },
  { path: '/privacy', priority: 0.5, changeFrequency: 'yearly' },
  { path: '/terms', priority: 0.5, changeFrequency: 'yearly' },
];

export default function sitemap(): MetadataRoute.Sitemap {
  const now = new Date();
  const staticEntries: MetadataRoute.Sitemap = STATIC_PATHS.map((entry) => ({
    url: `${BRAND.baseUrl}${entry.path}`,
    lastModified: now,
    changeFrequency: entry.changeFrequency,
    priority: entry.priority,
  }));

  const toolEntries: MetadataRoute.Sitemap = TOOL_SEED.map((tool) => ({
    url: `${BRAND.baseUrl}/tools/${tool.slug}`,
    lastModified: new Date(tool.updatedAt),
    changeFrequency: 'monthly',
    priority: 0.8,
    alternates: {
      languages: Object.fromEntries(
        Object.entries(tool.localizedSlugs).map(([locale, slug]) => [
          locale,
          `${BRAND.baseUrl}/${locale}/${localizedToolsPath(locale)}/${slug}`,
        ]),
      ),
    },
  }));

  return [...staticEntries, ...toolEntries];
}

function localizedToolsPath(locale: string): string {
  switch (locale) {
    case 'es':
      return 'herramientas';
    case 'fr':
      return 'outils';
    case 'de':
      return 'werkzeuge';
    case 'pt':
      return 'ferramentas';
    default:
      return 'tools';
  }
}
