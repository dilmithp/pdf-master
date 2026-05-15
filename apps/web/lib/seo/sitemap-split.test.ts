import { describe, expect, it } from 'vitest';
import { chunkSitemap, sitemapCount } from './sitemap-split';
import type { SitemapEntry } from './types';

function makeEntries(n: number): SitemapEntry[] {
  return Array.from({ length: n }, (_, i) => ({
    url: `https://example.com/page-${i}`,
    lastModified: new Date('2026-01-01T00:00:00Z'),
    changeFrequency: 'monthly' as const,
    priority: 0.5,
  }));
}

describe('chunkSitemap', () => {
  it('returns a single chunk when entries fit', () => {
    const chunks = chunkSitemap(makeEntries(10));
    expect(chunks).toHaveLength(1);
    expect(chunks[0]).toHaveLength(10);
  });

  it('splits exactly at the 50,000 boundary', () => {
    const chunks = chunkSitemap(makeEntries(100_001));
    expect(chunks).toHaveLength(3);
    expect(chunks[0]).toHaveLength(50_000);
    expect(chunks[1]).toHaveLength(50_000);
    expect(chunks[2]).toHaveLength(1);
  });

  it('respects a custom chunk size', () => {
    const chunks = chunkSitemap(makeEntries(7), 3);
    expect(chunks).toHaveLength(3);
    expect(chunks.map((c) => c.length)).toEqual([3, 3, 1]);
  });

  it('throws on non-positive chunk size', () => {
    expect(() => chunkSitemap([], 0)).toThrow(RangeError);
    expect(() => chunkSitemap([], -1)).toThrow(RangeError);
  });

  it('returns an empty array for empty input', () => {
    expect(chunkSitemap([])).toEqual([]);
  });
});

describe('sitemapCount', () => {
  it('returns the length of the entries array', () => {
    expect(sitemapCount(makeEntries(42))).toBe(42);
  });
});
