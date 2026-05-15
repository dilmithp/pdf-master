import { BRAND } from '@/lib/brand';
import type { Metadata, Viewport } from 'next';
import './globals.css';

export const metadata: Metadata = {
  metadataBase: new URL(BRAND.baseUrl),
  title: {
    default: `${BRAND.name} — Edit, Convert, and Sign PDFs Online`,
    template: `%s | ${BRAND.name}`,
  },
  description:
    'Privacy-first PDF productivity platform. Merge, split, compress, convert, sign, and chat with PDFs online. Files deleted in 60 minutes. No signup required.',
  applicationName: BRAND.name,
  authors: [{ name: BRAND.legalName }],
  creator: BRAND.legalName,
  publisher: BRAND.legalName,
  formatDetection: { email: false, address: false, telephone: false },
  openGraph: {
    type: 'website',
    siteName: BRAND.name,
    locale: 'en_US',
  },
  twitter: {
    card: 'summary_large_image',
    site: BRAND.social.twitter,
  },
};

export const viewport: Viewport = {
  width: 'device-width',
  initialScale: 1,
  themeColor: [
    { media: '(prefers-color-scheme: light)', color: '#ffffff' },
    { media: '(prefers-color-scheme: dark)', color: '#0a0a0a' },
  ],
};

const organizationJsonLd = {
  '@context': 'https://schema.org',
  '@type': 'Organization',
  '@id': `${BRAND.baseUrl}#organization`,
  name: BRAND.name,
  legalName: BRAND.legalName,
  url: BRAND.baseUrl,
  logo: `${BRAND.baseUrl}/logo.png`,
  sameAs: [BRAND.social.github, `https://twitter.com/${BRAND.social.twitter.replace('@', '')}`],
  contactPoint: {
    '@type': 'ContactPoint',
    contactType: 'customer support',
    email: BRAND.supportEmail,
    availableLanguage: BRAND.supportedLocales,
  },
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className="min-h-screen bg-white text-neutral-900 antialiased dark:bg-neutral-950 dark:text-neutral-100">
        <script
          type="application/ld+json"
          // biome-ignore lint/security/noDangerouslySetInnerHtml: JSON-LD is a primitive serialized via JSON.stringify, no injection vector
          dangerouslySetInnerHTML={{ __html: JSON.stringify(organizationJsonLd) }}
        />
        {children}
      </body>
    </html>
  );
}
