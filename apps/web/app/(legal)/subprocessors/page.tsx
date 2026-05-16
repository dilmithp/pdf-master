import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Subprocessors',
  description: `List of third-party subprocessors used by ${BRAND.name} to deliver our PDF services.`,
};

const LAST_UPDATED = '2026-05-16';

interface Subprocessor {
  name: string;
  purpose: string;
  location: string;
  dpaUrl: string;
}

const SUBPROCESSORS: Subprocessor[] = [
  {
    name: 'Amazon Web Services (AWS)',
    purpose:
      'Cloud infrastructure (compute, S3 file storage, RDS database, EKS container orchestration)',
    location: 'United States (us-east-1), EU (eu-west-1)',
    dpaUrl: 'https://aws.amazon.com/agreement/',
  },
  {
    name: 'Stripe',
    purpose: 'Payment processing and subscription management',
    location: 'United States',
    dpaUrl: 'https://stripe.com/privacy',
  },
  {
    name: 'Clerk',
    purpose: 'User authentication and session management',
    location: 'United States',
    dpaUrl: 'https://clerk.com/privacy',
  },
  {
    name: 'Anthropic',
    purpose: 'AI features: PDF summarisation, Q&A, and content extraction (Claude API)',
    location: 'United States',
    dpaUrl: 'https://www.anthropic.com/privacy',
  },
  {
    name: 'Postmark',
    purpose: 'Transactional email delivery (account verification, receipts, notifications)',
    location: 'United States',
    dpaUrl: 'https://postmarkapp.com/privacy-policy',
  },
  {
    name: 'Cloudflare',
    purpose: 'CDN, DDoS protection, WAF, and DNS',
    location: 'United States (global edge network)',
    dpaUrl: 'https://www.cloudflare.com/privacypolicy/',
  },
];

export default function SubprocessorsPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Subprocessors</h1>
      <p className="text-sm text-neutral-500">Last updated: {LAST_UPDATED}</p>

      <p>
        {BRAND.name} uses the following third-party subprocessors to deliver our services. Each
        subprocessor is bound by a Data Processing Agreement (DPA) and is only authorised to
        process personal data as instructed by us and as described below.
      </p>
      <p>
        If you are an enterprise customer and require a copy of our DPA or any subprocessor DPAs,
        please contact <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
      </p>

      <div className="not-prose overflow-x-auto">
        <table className="w-full border-collapse text-sm">
          <thead>
            <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
              <th className="py-2 pr-4 font-semibold">Subprocessor</th>
              <th className="py-2 pr-4 font-semibold">Purpose</th>
              <th className="py-2 pr-4 font-semibold">Location</th>
              <th className="py-2 font-semibold">DPA / Privacy Policy</th>
            </tr>
          </thead>
          <tbody>
            {SUBPROCESSORS.map((sp) => (
              <tr
                key={sp.name}
                className="border-b border-neutral-100 dark:border-neutral-800"
              >
                <td className="py-3 pr-4 font-medium">{sp.name}</td>
                <td className="py-3 pr-4 text-neutral-600 dark:text-neutral-400">{sp.purpose}</td>
                <td className="py-3 pr-4">{sp.location}</td>
                <td className="py-3">
                  <a
                    href={sp.dpaUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-blue-600 underline hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-200"
                  >
                    View DPA
                  </a>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <h2>Updates to This List</h2>
      <p>
        We may update this list as our subprocessors change. Significant changes (additions of
        subprocessors that process personal data) will be announced via email to account holders
        at least 14 days before the change takes effect, giving you the opportunity to object.
      </p>

      <h2>International Transfers</h2>
      <p>
        Where subprocessors process personal data outside the EEA, transfers are protected by
        Standard Contractual Clauses (SCCs) approved by the European Commission. EU users&apos;
        data is routed to eu-west-1 (Ireland) for primary processing; transfers to US-based
        subprocessors rely on SCCs.
      </p>
    </article>
  );
}
