import { describe, expect, it } from 'vitest';
import { TOOL_SEED, getRelatedTools, getToolBySlug } from './seed';

describe('TOOL_SEED', () => {
  it('contains the 10 launch tools', () => {
    expect(TOOL_SEED.map((t) => t.slug)).toEqual([
      'merge-pdf',
      'split-pdf',
      'compress-pdf',
      'rotate-pdf',
      'jpg-to-pdf',
      'pdf-to-jpg',
      'word-to-pdf',
      'pdf-to-word',
      'add-page-numbers',
      'add-watermark',
    ]);
  });

  it('has unique slugs', () => {
    const slugs = TOOL_SEED.map((t) => t.slug);
    expect(new Set(slugs).size).toBe(slugs.length);
  });

  it('every tool has at least 5 FAQs and 3 how-to steps', () => {
    for (const tool of TOOL_SEED) {
      expect(tool.faqs.length).toBeGreaterThanOrEqual(5);
      expect(tool.howToSteps.length).toBeGreaterThanOrEqual(3);
    }
  });

  it('every tool has all four localized slugs', () => {
    for (const tool of TOOL_SEED) {
      for (const locale of ['es', 'fr', 'de', 'pt'] as const) {
        expect(tool.localizedSlugs[locale], `missing ${locale} slug for ${tool.slug}`).toBeTruthy();
      }
    }
  });
});

describe('getToolBySlug', () => {
  it('returns the tool for a known slug', () => {
    expect(getToolBySlug('merge-pdf')?.h1).toBe('Merge PDF Files Online');
  });

  it('returns undefined for an unknown slug', () => {
    expect(getToolBySlug('does-not-exist')).toBeUndefined();
  });
});

describe('getRelatedTools', () => {
  it('returns tools for known slugs only', () => {
    const related = getRelatedTools(['merge-pdf', 'unknown-slug', 'split-pdf']);
    expect(related.map((t) => t.slug)).toEqual(['merge-pdf', 'split-pdf']);
  });
});
