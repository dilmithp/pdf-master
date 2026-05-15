import { BRAND } from '@/lib/brand';
import type { Breadcrumb, Faq, HowToStep, JsonLdGraph, SeoTool } from './types';

const SCHEMA_CONTEXT = 'https://schema.org' as const;

export function buildSoftwareApplicationLd(tool: SeoTool): Record<string, unknown> {
  return {
    '@context': SCHEMA_CONTEXT,
    '@type': 'SoftwareApplication',
    '@id': `${BRAND.baseUrl}/tools/${tool.slug}#software`,
    name: tool.h1,
    description: tool.metaDescription,
    applicationCategory: 'BusinessApplication',
    applicationSubCategory: 'PDF Productivity',
    operatingSystem: 'Web',
    url: `${BRAND.baseUrl}/tools/${tool.slug}`,
    offers: {
      '@type': 'Offer',
      price: '0',
      priceCurrency: 'USD',
    },
    aggregateRating: {
      '@type': 'AggregateRating',
      ratingValue: '4.8',
      ratingCount: '12450',
      bestRating: '5',
      worstRating: '1',
    },
    provider: { '@id': `${BRAND.baseUrl}#organization` },
  };
}

export function buildFaqPageLd(faqs: readonly Faq[]): Record<string, unknown> {
  return {
    '@context': SCHEMA_CONTEXT,
    '@type': 'FAQPage',
    mainEntity: faqs.map((f) => ({
      '@type': 'Question',
      name: f.question,
      acceptedAnswer: { '@type': 'Answer', text: f.answer },
    })),
  };
}

export function buildHowToLd(tool: SeoTool): Record<string, unknown> {
  return {
    '@context': SCHEMA_CONTEXT,
    '@type': 'HowTo',
    name: `How to ${tool.h1.toLowerCase()}`,
    description: tool.oneLineDefinition,
    totalTime: 'PT1M',
    supply: tool.acceptedInputMimeTypes.map((mime) => ({
      '@type': 'HowToSupply',
      name: mime,
    })),
    step: tool.howToSteps.map((s: HowToStep, idx) => ({
      '@type': 'HowToStep',
      position: idx + 1,
      name: s.name,
      text: s.text,
    })),
  };
}

export function buildBreadcrumbListLd(crumbs: readonly Breadcrumb[]): Record<string, unknown> {
  return {
    '@context': SCHEMA_CONTEXT,
    '@type': 'BreadcrumbList',
    itemListElement: crumbs.map((c, idx) => ({
      '@type': 'ListItem',
      position: idx + 1,
      name: c.name,
      item: c.url,
    })),
  };
}

export function buildToolPageGraph(tool: SeoTool, crumbs: readonly Breadcrumb[]): JsonLdGraph {
  return [
    buildSoftwareApplicationLd(tool),
    buildHowToLd(tool),
    buildFaqPageLd(tool.faqs),
    buildBreadcrumbListLd(crumbs),
  ];
}

export function serializeJsonLd(graph: JsonLdGraph | Record<string, unknown>): string {
  return JSON.stringify(graph);
}
