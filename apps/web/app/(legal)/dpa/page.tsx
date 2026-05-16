import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Data Processing Agreement (DPA)',
  description: `Data Processing Agreement for ${BRAND.name} enterprise customers. Available on request.`,
};

const LAST_UPDATED = '2026-05-16';

export default function DpaPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Data Processing Agreement (DPA)</h1>
      <p className="text-sm text-neutral-500">Last updated: {LAST_UPDATED}</p>

      <p>
        A Data Processing Agreement (DPA) is available to enterprise customers of {BRAND.name} who
        process personal data on behalf of their own end users and require a formal GDPR Article
        28 agreement with us as data processor.
      </p>

      <h2>How to Request a DPA</h2>
      <p>
        DPAs are available on request to customers on the Business or Enterprise plan. To request
        a DPA:
      </p>
      <ol>
        <li>
          Email <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a> with the subject line
          &quot;DPA Request — [your organisation name]&quot;.
        </li>
        <li>Include your registered company name, country, and {BRAND.name} account email.</li>
        <li>We will respond within 5 business days with a signed DPA.</li>
      </ol>

      <h2>DPA Template</h2>
      <p>
        A template DPA based on the European Commission&apos;s Standard Contractual Clauses (SCCs)
        will be available for download at this page once our legal review is complete.
      </p>
      <p>
        [Placeholder: Link to signed DPA PDF will appear here — replace before launch]
      </p>

      <h2>Self-Serve DPA</h2>
      <p>
        For customers who prefer a self-serve process: once our DPA is finalised, you will be able
        to accept it directly from your account settings under Settings &rarr; Security &amp;
        Privacy &rarr; Data Processing Agreement.
      </p>

      <h2>What Our DPA Covers</h2>
      <ul>
        <li>
          Subject matter, nature, and purpose of processing in accordance with GDPR Article 28.
        </li>
        <li>
          Our obligations as data processor: processing only on your instructions, confidentiality,
          security measures, and subprocessor management.
        </li>
        <li>
          Your obligations as data controller: ensuring a valid legal basis for processing and
          complying with data subject rights requests.
        </li>
        <li>International transfer mechanisms (SCCs for transfers outside the EEA).</li>
        <li>Data breach notification obligations (within 72 hours of our becoming aware).</li>
        <li>
          Our list of{' '}
          <a href="/legal/subprocessors">approved subprocessors</a>.
        </li>
      </ul>

      <h2>Contact</h2>
      <address className="not-italic">
        {BRAND.companyEntity}
        <br />
        {BRAND.legalAddress}
        <br />
        <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>
      </address>
    </article>
  );
}
