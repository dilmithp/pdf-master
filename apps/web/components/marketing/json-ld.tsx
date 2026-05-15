import { serializeJsonLd } from '@/lib/seo/schema';
import type { JsonLdGraph } from '@/lib/seo/types';

interface JsonLdProps {
  graph: JsonLdGraph | Record<string, unknown>;
}

export function JsonLd({ graph }: JsonLdProps) {
  return (
    <script
      type="application/ld+json"
      // biome-ignore lint/security/noDangerouslySetInnerHtml: JSON-LD payload is produced by serializeJsonLd which calls JSON.stringify on plain objects, no injection vector
      dangerouslySetInnerHTML={{ __html: serializeJsonLd(graph) }}
    />
  );
}
