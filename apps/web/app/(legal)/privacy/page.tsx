import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Privacy Policy',
  description: `Privacy Policy for ${BRAND.name}. Learn how we collect, use, and protect your personal data under GDPR and CCPA.`,
};

const LAST_UPDATED = '2026-05-16';

export default function PrivacyPolicyPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Privacy Policy</h1>
      <p className="text-sm text-neutral-500">Last updated: {LAST_UPDATED}</p>

      {/* 1. Who We Are */}
      <h2>1. Who We Are</h2>
      <p>
        {BRAND.companyEntity} (&quot;{BRAND.name}&quot;, &quot;we&quot;, &quot;us&quot;, or
        &quot;our&quot;) is the data controller for personal data processed through our platform,
        accessible at{' '}
        <a href={`https://${BRAND.domain}`}>{BRAND.domain}</a>.
      </p>
      <p>
        <strong>Registered address:</strong> {BRAND.legalAddress}
      </p>
      <p>
        <strong>Data Protection Officer:</strong>{' '}
        <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>
      </p>

      {/* 2. Personal Data We Collect */}
      <h2>2. Personal Data We Collect</h2>
      <p>We collect the following categories of personal data:</p>
      <ul>
        <li>
          <strong>Account data:</strong> Email address, name, and password hash when you create an
          account.
        </li>
        <li>
          <strong>Payment data:</strong> Billing name, address, and last four digits of payment
          card. Full card details are processed exclusively by our payment processor (Stripe) and
          never stored on our servers.
        </li>
        <li>
          <strong>Uploaded files:</strong> PDF and other documents you upload for processing.
          These are encrypted at rest and automatically deleted within 60 minutes of upload.
        </li>
        <li>
          <strong>Usage data:</strong> IP address, browser type, pages visited, feature usage, and
          error logs for service improvement and security monitoring.
        </li>
        <li>
          <strong>Cookie data:</strong> Session identifiers and, if you consent, analytics
          identifiers. See our{' '}
          <a href="/legal/cookies">Cookie Policy</a> for details.
        </li>
      </ul>

      {/* 3. Why We Process */}
      <h2>3. Why We Process Your Data (Lawful Bases)</h2>
      <p>We process your personal data on the following lawful bases under GDPR Article 6:</p>
      <ul>
        <li>
          <strong>Contract (Art. 6(1)(b)):</strong> To provide the PDF processing services you
          requested and manage your account.
        </li>
        <li>
          <strong>Legal obligation (Art. 6(1)(c)):</strong> To comply with applicable laws, such
          as retaining billing records for tax purposes.
        </li>
        <li>
          <strong>Legitimate interest (Art. 6(1)(f)):</strong> To detect and prevent fraud, abuse,
          and security incidents; to improve service performance; and to send transactional
          communications.
        </li>
        <li>
          <strong>Consent (Art. 6(1)(a)):</strong> To deliver analytics cookies and marketing
          communications, where you have given explicit consent. You may withdraw consent at any
          time.
        </li>
      </ul>

      {/* 4. Retention */}
      <h2>4. How Long We Retain Your Data</h2>
      <ul>
        <li>
          <strong>Uploaded files:</strong> Automatically deleted within 60 minutes of upload,
          regardless of account status.
        </li>
        <li>
          <strong>Account data:</strong> Retained until you delete your account. Upon deletion,
          data is purged within 30 days except where a legal obligation requires longer retention.
        </li>
        <li>
          <strong>Billing records:</strong> Retained for 7 years in compliance with applicable tax
          and accounting laws.
        </li>
        <li>
          <strong>Server logs:</strong> Retained for 90 days for security and debugging purposes.
        </li>
      </ul>

      {/* 5. International Transfers */}
      <h2>5. International Data Transfers</h2>
      <p>
        We operate primarily on Amazon Web Services (AWS) in two regions:{' '}
        <strong>us-east-1</strong> (Virginia, USA) and <strong>eu-west-1</strong> (Ireland, EU).
      </p>
      <p>
        Users located in the European Union or EEA have their data processed in eu-west-1 by
        default. This data residency hint is applied automatically based on your IP address.
      </p>
      <p>
        Where personal data is transferred outside the EEA, we rely on{' '}
        <strong>Standard Contractual Clauses (SCCs)</strong> approved by the European Commission
        as the transfer mechanism. A list of our subprocessors and their locations is available at{' '}
        <a href="/legal/subprocessors">our Subprocessors page</a>.
      </p>

      {/* 6. Your Rights */}
      <h2>6. Your Rights</h2>
      <p>
        Under GDPR (and equivalent CCPA rights for California residents), you have the following
        rights:
      </p>
      <ul>
        <li>
          <strong>Right of access (Art. 15):</strong> Request a copy of the personal data we hold
          about you.
        </li>
        <li>
          <strong>Right to rectification (Art. 16):</strong> Correct inaccurate or incomplete data.
        </li>
        <li>
          <strong>Right to erasure (Art. 17):</strong> Request deletion of your personal data
          (&quot;right to be forgotten&quot;).
        </li>
        <li>
          <strong>Right to data portability (Art. 20):</strong> Receive your data in a
          machine-readable format.
        </li>
        <li>
          <strong>Right to restriction (Art. 18):</strong> Limit how we process your data in
          certain circumstances.
        </li>
        <li>
          <strong>Right to object (Art. 21):</strong> Object to processing based on legitimate
          interests or for direct marketing.
        </li>
        <li>
          <strong>Right to withdraw consent:</strong> Withdraw any previously given consent at
          any time without affecting the lawfulness of prior processing.
        </li>
      </ul>
      <p>
        To exercise any of these rights, contact{' '}
        <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>. We will respond within 30 days.
        You also have the right to lodge a complaint with your local supervisory authority.
      </p>

      {/* 7. Subprocessors */}
      <h2>7. Subprocessors</h2>
      <p>
        We engage trusted third-party subprocessors to help deliver our services. A complete list
        including their purpose, location, and Data Processing Agreement is available at{' '}
        <a href="/legal/subprocessors">pdfmaster.app/legal/subprocessors</a>.
      </p>

      {/* 8. Cookies */}
      <h2>8. Cookies</h2>
      <p>
        We use essential cookies for authentication and session management, and optional analytics
        cookies subject to your consent. For full details, see our{' '}
        <a href="/legal/cookies">Cookie Policy</a>.
      </p>

      {/* 9. Children */}
      <h2>9. Children&apos;s Data</h2>
      <p>
        Our services are not directed at children under 16 years of age. We do not knowingly
        collect personal data from children under 16. If we become aware that we have
        inadvertently collected such data, we will delete it promptly. If you believe a child has
        provided us with personal data, please contact{' '}
        <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
      </p>

      {/* 10. Changes */}
      <h2>10. Changes to This Policy</h2>
      <p>
        We may update this Privacy Policy from time to time. When we make material changes, we
        will notify you by email (sent to the address associated with your account) and update
        the &quot;Last updated&quot; date at the top of this page. Continued use of the service
        after the effective date of any changes constitutes acceptance of the updated policy.
      </p>

      {/* 11. Contact */}
      <h2>11. Contact</h2>
      <p>
        For privacy-related enquiries or to exercise your rights, contact our Data Protection
        Officer:
      </p>
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
