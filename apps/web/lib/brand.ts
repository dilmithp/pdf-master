export const BRAND = {
  name: 'PDFMaster',
  legalName: 'PDFMaster, Inc.',
  domain: 'pdfmaster.app',
  baseUrl: process.env.NEXT_PUBLIC_BASE_URL ?? 'https://pdfmaster.app',
  supportEmail: 'support@pdfmaster.app',
  /** Data Protection Officer contact email */
  dpoEmail: 'privacy@pdfmaster.app',
  /** Security vulnerability disclosure email */
  securityEmail: 'security@pdfmaster.app',
  /** Registered legal entity name */
  companyEntity: 'PDFMaster, Inc.',
  /** Registered company address — replace with actual legal address before launch */
  legalAddress: '123 Placeholder Street, Wilmington, DE 19801, United States',
  social: {
    twitter: '@pdfmaster',
    github: 'https://github.com/pdfmaster',
  },
  defaultLocale: 'en' as const,
  supportedLocales: ['en', 'es', 'fr', 'de', 'pt'] as const,
} as const;

export type Locale = (typeof BRAND.supportedLocales)[number];
