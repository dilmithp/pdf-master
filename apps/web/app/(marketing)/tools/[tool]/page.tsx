import { Breadcrumbs } from '@/components/marketing/breadcrumbs';
import { FaqList } from '@/components/marketing/faq-list';
import { HowToList } from '@/components/marketing/how-to-list';
import { JsonLd } from '@/components/marketing/json-ld';
import { RelatedTools } from '@/components/marketing/related-tools';
import { ToolDropzone } from '@/components/marketing/tool-dropzone';
import { TrustStrip } from '@/components/marketing/trust-strip';
import { BRAND } from '@/lib/brand';
import { buildToolPageGraph } from '@/lib/seo/schema';
import type { Breadcrumb } from '@/lib/seo/types';
import { TOOL_SEED, getRelatedTools, getToolBySlug } from '@/lib/tools/seed';
import type { Metadata } from 'next';
import { notFound } from 'next/navigation';

interface ToolPageProps {
  params: Promise<{ tool: string }>;
}

export function generateStaticParams(): { tool: string }[] {
  return TOOL_SEED.map((t) => ({ tool: t.slug }));
}

export async function generateMetadata({ params }: ToolPageProps): Promise<Metadata> {
  const { tool: slug } = await params;
  const tool = getToolBySlug(slug);
  if (!tool) return {};

  const languages = Object.fromEntries(
    Object.entries(tool.localizedSlugs).map(([locale, localizedSlug]) => [
      locale,
      `${BRAND.baseUrl}/${locale}/${localizedToolsPath(locale)}/${localizedSlug}`,
    ]),
  );

  return {
    title: tool.metaTitle,
    description: tool.metaDescription,
    alternates: {
      canonical: `/tools/${tool.slug}`,
      languages: { 'x-default': `${BRAND.baseUrl}/tools/${tool.slug}`, ...languages },
    },
    openGraph: {
      type: 'website',
      url: `${BRAND.baseUrl}/tools/${tool.slug}`,
      title: tool.ogTitle,
      description: tool.ogDescription,
      images: [{ url: tool.ogImage, width: 1200, height: 630, alt: tool.h1 }],
    },
    twitter: {
      card: 'summary_large_image',
      title: tool.ogTitle,
      description: tool.ogDescription,
      images: [tool.ogImage],
    },
  };
}

export default async function ToolPage({ params }: ToolPageProps) {
  const { tool: slug } = await params;
  const tool = getToolBySlug(slug);
  if (!tool) notFound();

  const related = getRelatedTools(tool.relatedSlugs);
  const crumbs: Breadcrumb[] = [
    { name: 'Home', url: `${BRAND.baseUrl}/` },
    { name: 'Tools', url: `${BRAND.baseUrl}/tools` },
    { name: tool.h1, url: `${BRAND.baseUrl}/tools/${tool.slug}` },
  ];

  return (
    <article className="mx-auto max-w-4xl px-4 py-12">
      <JsonLd graph={buildToolPageGraph(tool, crumbs)} />
      <Breadcrumbs crumbs={crumbs} />
      <header className="text-center">
        <h1 className="text-balance text-4xl font-semibold tracking-tight sm:text-5xl">
          {tool.h1}
        </h1>
        <p className="mx-auto mt-4 max-w-2xl text-pretty text-lg text-neutral-600 dark:text-neutral-400">
          {tool.oneLineDefinition}
        </p>
        <TrustStrip />
      </header>

      <div className="mt-10">
        <ToolDropzone tool={tool} />
      </div>

      <section className="prose prose-neutral mt-12 max-w-none dark:prose-invert">
        <h2 className="text-2xl font-semibold tracking-tight">About {tool.h1}</h2>
        <p className="mt-4 text-neutral-700 dark:text-neutral-300">{tool.longDescription}</p>
      </section>

      <HowToList title={`How to ${tool.h1.toLowerCase()}`} steps={tool.howToSteps} />
      <FaqList faqs={tool.faqs} />
      <RelatedTools tools={related} />
    </article>
  );
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
