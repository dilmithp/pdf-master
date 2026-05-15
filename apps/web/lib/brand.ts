export const BRAND = {
  name: 'PDFMaster',
  legalName: 'PDFMaster, Inc.',
  domain: 'pdfmaster.app',
  baseUrl: process.env.NEXT_PUBLIC_BASE_URL ?? 'https://pdfmaster.app',
  supportEmail: 'support@pdfmaster.app',
  social: {
    twitter: '@pdfmaster',
    github: 'https://github.com/pdfmaster',
  },
  defaultLocale: 'en' as const,
  supportedLocales: ['en', 'es', 'fr', 'de', 'pt'] as const,
} as const;

export type Locale = (typeof BRAND.supportedLocales)[number];
