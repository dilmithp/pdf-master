import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Subprocessors',
  description: `Complete list of third-party subprocessors used by ${BRAND.name}, including data categories, locations, and DPA links.`,
};

const LAST_UPDATED = '2026-05-16';

interface Subprocessor {
  name: string;
  purpose: string;
  dataCategories: string;
  location: string;
  dpaUrl: string;
  lastReviewed: string;
}

const SUBPROCESSORS: Subprocessor[] = [
  {
    name: 'Amazon Web Services (AWS)',
    purpose:
      'Cloud infrastructure: compute (EKS), file storage (S3), managed database (RDS PostgreSQL), caching (ElastiCache/Redis), container orchestration, secrets management (Secrets Manager)',
    dataCategories: 'All user data processed on the platform (account data, uploaded files, logs)',
    location: 'United States (us-east-1, Virginia); EU (eu-west-1, Ireland)',
    dpaUrl: 'https://aws.amazon.com/agreement/',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'Stripe',
    purpose: 'Payment processing, subscription billing management, invoicing, and tax calculation',
    dataCategories: 'Payment card details (last 4 digits only — full card data goes directly to Stripe), billing name, billing address, email',
    location: 'United States; EU processing via Stripe Payments Europe Ltd (Ireland)',
    dpaUrl: 'https://stripe.com/privacy',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'Clerk',
    purpose: 'User authentication, session management, MFA, OAuth integration (Google, GitHub)',
    dataCategories: 'Email address, authentication tokens, session identifiers, MFA preferences',
    location: 'United States',
    dpaUrl: 'https://clerk.com/legal/dpa',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'Anthropic',
    purpose:
      'AI language model completions for AI features: PDF summarisation, document Q&A, content extraction (Claude API)',
    dataCategories:
      'Text content extracted from user-uploaded documents — transmitted only when AI features are explicitly invoked',
    location: 'United States',
    dpaUrl: 'https://www.anthropic.com/legal/aup',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'OpenAI',
    purpose:
      'Vector embeddings generation for semantic document search (Embeddings API). Embeddings stored in pgvector (AWS RDS); deleted on document expiry or account deletion.',
    dataCategories:
      'Text content extracted from user-uploaded documents — transmitted only when AI features are explicitly invoked',
    location: 'United States',
    dpaUrl: 'https://openai.com/policies/data-processing-addendum',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'Postmark (Wildbit / ActiveCampaign)',
    purpose:
      'Transactional email delivery: account verification, password reset, subscription receipts, system notifications',
    dataCategories: 'Recipient email address, email subject line, email body (contains account data)',
    location: 'United States',
    dpaUrl: 'https://postmarkapp.com/eu-privacy',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'Cloudflare',
    purpose:
      'Content delivery network (CDN), DDoS mitigation, Web Application Firewall (WAF), DNS management, bot management',
    dataCategories: 'IP addresses, HTTP request headers, request URLs (logs are transient at edge nodes)',
    location: 'Global edge network; EU traffic processed at EU PoPs where feasible',
    dpaUrl: 'https://www.cloudflare.com/cloudflare-customer-dpa/',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'Sentry',
    purpose:
      'Application error monitoring and performance monitoring. We configure Sentry to scrub PII from stack traces before transmission.',
    dataCategories:
      'Error stack traces, request URLs, browser/device type. PII (email, file content) is actively scrubbed via Sentry data scrubbing rules before transmission.',
    location: 'United States (Sentry EU option available — see note)',
    dpaUrl: 'https://sentry.io/legal/dpa/',
    lastReviewed: '2026-05-16',
  },
  {
    name: 'Vercel',
    purpose:
      'Frontend hosting and edge functions for the Next.js web application (apps/web)',
    dataCategories: 'HTTP request logs (IP address, user-agent, request path), build artefacts',
    location: 'United States; EU edge deployments where available',
    dpaUrl: 'https://vercel.com/legal/privacy-policy',
    lastReviewed: '2026-05-16',
  },
];

export default function SubprocessorsPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Subprocessors</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      <p>
        {BRAND.name} engages the following third-party sub-processors to help deliver our services.
        Each subprocessor is bound by a Data Processing Agreement (DPA) and is authorised to process
        personal data only as instructed by us, only for the stated purposes, and subject to
        appropriate technical and organisational security measures.
      </p>
      <p>
        Our use of subprocessors is governed by GDPR Article 28(2)-(4). We remain responsible for
        each subprocessor&apos;s compliance with GDPR obligations. For our full Data Processing
        Agreement (enterprise customers), see <a href="/legal/dpa">/legal/dpa</a> or contact{' '}
        <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
      </p>

      <section>
        <h2>Current Subprocessors</h2>
        <div className="not-prose overflow-x-auto">
          <table className="w-full border-collapse text-sm">
            <caption className="mb-2 text-left text-xs text-neutral-500">
              Subprocessor list — last reviewed {LAST_UPDATED}. DPA links open in a new tab.
            </caption>
            <thead>
              <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
                <th className="py-2 pr-4 font-semibold">Subprocessor</th>
                <th className="py-2 pr-4 font-semibold">Purpose</th>
                <th className="py-2 pr-4 font-semibold">Data Categories</th>
                <th className="py-2 pr-4 font-semibold">Location</th>
                <th className="py-2 pr-4 font-semibold">DPA / Privacy</th>
                <th className="py-2 font-semibold">Last Reviewed</th>
              </tr>
            </thead>
            <tbody>
              {SUBPROCESSORS.map((sp) => (
                <tr
                  key={sp.name}
                  className="border-b border-neutral-100 dark:border-neutral-800"
                >
                  <td className="py-3 pr-4 font-medium align-top">{sp.name}</td>
                  <td className="py-3 pr-4 text-neutral-600 dark:text-neutral-400 align-top text-xs">
                    {sp.purpose}
                  </td>
                  <td className="py-3 pr-4 text-neutral-600 dark:text-neutral-400 align-top text-xs">
                    {sp.dataCategories}
                  </td>
                  <td className="py-3 pr-4 align-top text-xs">{sp.location}</td>
                  <td className="py-3 pr-4 align-top">
                    <a
                      href={sp.dpaUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-blue-600 underline hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-200 text-xs"
                    >
                      View DPA
                    </a>
                  </td>
                  <td className="py-3 align-top text-xs text-neutral-500">{sp.lastReviewed}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      <section>
        <h2>International Transfer Mechanisms</h2>
        <p>
          Where subprocessors process personal data outside the EU/EEA or UK, we rely on the
          following transfer mechanisms:
        </p>
        <ul>
          <li>
            <strong>EU Standard Contractual Clauses (SCCs, Decision 2021/914/EU):</strong> All
            US-based subprocessors operate under the 2021 SCCs incorporating the appropriate module
            (controller-to-processor or processor-to-processor as applicable).
          </li>
          <li>
            <strong>UK IDTA Addendum:</strong> Transfers from the UK are covered by the UK
            International Data Transfer Agreement (IDTA) Addendum, effective 21 March 2022, appended
            to applicable subprocessor agreements.
          </li>
          <li>
            <strong>EU-US Data Privacy Framework (DPF):</strong> Where a subprocessor is certified
            under the DPF (recognised by the European Commission in Decision 2023/1795), we
            additionally rely on DPF certification. DPF-certified participants are listed at{' '}
            <a
              href="https://www.dataprivacyframework.gov/list"
              target="_blank"
              rel="noopener noreferrer"
            >
              dataprivacyframework.gov/list
            </a>
            .
          </li>
        </ul>
        <p>
          Note on Sentry: Sentry offers an EU data residency option. We are evaluating whether to
          migrate EU user error data to Sentry EU (Frankfurt) to avoid transatlantic transfer for
          that data stream. [TODO: Confirm and update before EU launch.]
        </p>
      </section>

      <section>
        <h2>Updates to This List</h2>
        <p>
          We will notify you <strong>at least 30 days in advance</strong> — via email to your
          registered account email address and via an in-app banner — before adding any new
          subprocessor that processes personal data, or before making any change to an existing
          subprocessor that materially affects the nature of processing.
        </p>
        <p>
          You have the right to <strong>object</strong> to the addition of a new subprocessor. If
          you object and we cannot provide the Service without the new subprocessor, you may
          terminate your subscription before the change takes effect and receive a pro-rated refund
          of any prepaid fees for the unused period.
        </p>
        <p>
          Non-material changes (e.g., subprocessor company rebrand, DPA URL update, security
          improvement) will be reflected in this list at the next review date without separate
          advance notice.
        </p>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
