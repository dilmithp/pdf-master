import type { Locale } from '@/lib/brand';

export interface SeoTool {
  slug: string;
  category: ToolCategory;
  primaryKeyword: string;
  h1: string;
  metaTitle: string;
  metaDescription: string;
  ogTitle: string;
  ogDescription: string;
  ogImage: string;
  oneLineDefinition: string;
  longDescription: string;
  howToSteps: HowToStep[];
  faqs: Faq[];
  relatedSlugs: string[];
  acceptedInputMimeTypes: string[];
  outputMimeType: string;
  maxFreeFileSizeMb: number;
  maxPaidFileSizeMb: number;
  localizedSlugs: Partial<Record<Locale, string>>;
  publishedAt: string;
  updatedAt: string;
}

export type ToolCategory =
  | 'organize'
  | 'optimize'
  | 'convert-to-pdf'
  | 'convert-from-pdf'
  | 'edit'
  | 'security'
  | 'ai';

export interface HowToStep {
  name: string;
  text: string;
}

export interface Faq {
  question: string;
  answer: string;
}

export interface Breadcrumb {
  name: string;
  url: string;
}

export interface SitemapEntry {
  url: string;
  lastModified: Date;
  changeFrequency: 'always' | 'hourly' | 'daily' | 'weekly' | 'monthly' | 'yearly' | 'never';
  priority: number;
  alternates?: {
    languages: Record<string, string>;
  };
}

export type JsonLdGraph = ReadonlyArray<Record<string, unknown>>;
