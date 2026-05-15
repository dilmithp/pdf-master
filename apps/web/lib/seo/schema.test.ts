import { TOOL_SEED } from '@/lib/tools/seed';
import { describe, expect, it } from 'vitest';
import {
  buildBreadcrumbListLd,
  buildFaqPageLd,
  buildHowToLd,
  buildSoftwareApplicationLd,
  buildToolPageGraph,
  serializeJsonLd,
} from './schema';

const SAMPLE_TOOL = TOOL_SEED[0];
if (!SAMPLE_TOOL) throw new Error('TOOL_SEED is empty — cannot run schema tests');

describe('buildSoftwareApplicationLd', () => {
  it('produces schema.org SoftwareApplication JSON-LD with required fields', () => {
    const ld = buildSoftwareApplicationLd(SAMPLE_TOOL);
    expect(ld['@context']).toBe('https://schema.org');
    expect(ld['@type']).toBe('SoftwareApplication');
    expect(ld.name).toBe(SAMPLE_TOOL.h1);
    expect(ld.applicationCategory).toBe('BusinessApplication');
    expect(ld.operatingSystem).toBe('Web');
    expect(ld.offers).toMatchObject({ '@type': 'Offer', price: '0', priceCurrency: 'USD' });
  });
});

describe('buildFaqPageLd', () => {
  it('emits a FAQPage with one Question per FAQ', () => {
    const ld = buildFaqPageLd(SAMPLE_TOOL.faqs);
    expect(ld['@type']).toBe('FAQPage');
    const entities = ld.mainEntity as ReadonlyArray<{
      '@type': string;
      name: string;
      acceptedAnswer: { '@type': string; text: string };
    }>;
    expect(entities).toHaveLength(SAMPLE_TOOL.faqs.length);
    for (const [idx, q] of entities.entries()) {
      const expected = SAMPLE_TOOL.faqs[idx];
      if (!expected) throw new Error(`missing expected FAQ at index ${idx}`);
      expect(q['@type']).toBe('Question');
      expect(q.name).toBe(expected.question);
      expect(q.acceptedAnswer['@type']).toBe('Answer');
      expect(q.acceptedAnswer.text).toBe(expected.answer);
    }
  });
});

describe('buildHowToLd', () => {
  it('emits a HowTo with positioned steps', () => {
    const ld = buildHowToLd(SAMPLE_TOOL);
    expect(ld['@type']).toBe('HowTo');
    const steps = ld.step as ReadonlyArray<{ '@type': string; position: number; name: string }>;
    expect(steps).toHaveLength(SAMPLE_TOOL.howToSteps.length);
    expect(steps[0]?.position).toBe(1);
    expect(steps.at(-1)?.position).toBe(SAMPLE_TOOL.howToSteps.length);
  });
});

describe('buildBreadcrumbListLd', () => {
  it('emits a BreadcrumbList with ascending positions', () => {
    const ld = buildBreadcrumbListLd([
      { name: 'Home', url: 'https://example.com/' },
      { name: 'Tools', url: 'https://example.com/tools' },
      { name: 'Merge PDF', url: 'https://example.com/tools/merge-pdf' },
    ]);
    expect(ld['@type']).toBe('BreadcrumbList');
    const items = ld.itemListElement as ReadonlyArray<{ position: number; name: string }>;
    expect(items.map((i) => i.position)).toEqual([1, 2, 3]);
    expect(items[2]?.name).toBe('Merge PDF');
  });
});

describe('buildToolPageGraph', () => {
  it('returns the four required JSON-LD documents in order', () => {
    const graph = buildToolPageGraph(SAMPLE_TOOL, [
      { name: 'Home', url: 'https://example.com/' },
      { name: 'Tools', url: 'https://example.com/tools' },
      { name: SAMPLE_TOOL.h1, url: `https://example.com/tools/${SAMPLE_TOOL.slug}` },
    ]);
    expect(graph).toHaveLength(4);
    expect(graph[0]?.['@type']).toBe('SoftwareApplication');
    expect(graph[1]?.['@type']).toBe('HowTo');
    expect(graph[2]?.['@type']).toBe('FAQPage');
    expect(graph[3]?.['@type']).toBe('BreadcrumbList');
  });
});

describe('serializeJsonLd', () => {
  it('produces valid JSON without HTML-unsafe sequences', () => {
    const out = serializeJsonLd(buildSoftwareApplicationLd(SAMPLE_TOOL));
    expect(() => JSON.parse(out)).not.toThrow();
    expect(out).not.toMatch(/<\/script/i);
  });
});
