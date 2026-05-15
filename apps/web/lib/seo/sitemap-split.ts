import type { SitemapEntry } from './types';

const MAX_URLS_PER_SITEMAP = 50_000;

export function chunkSitemap(
  entries: readonly SitemapEntry[],
  chunkSize: number = MAX_URLS_PER_SITEMAP,
): SitemapEntry[][] {
  if (chunkSize <= 0) throw new RangeError('chunkSize must be positive');
  const chunks: SitemapEntry[][] = [];
  for (let i = 0; i < entries.length; i += chunkSize) {
    chunks.push([...entries.slice(i, i + chunkSize)]);
  }
  return chunks;
}

export function sitemapCount(entries: readonly SitemapEntry[]): number {
  return entries.length;
}
