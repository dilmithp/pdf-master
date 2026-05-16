import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Terms of Service',
  description: `Terms of Service for ${BRAND.name}. Read our terms for using our PDF productivity platform.`,
};

const LAST_UPDATED = '2026-05-16';

export default function TermsOfServicePage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Terms of Service</h1>
      <p className="text-sm text-neutral-500">Last updated: {LAST_UPDATED}</p>

      {/* 1. Acceptance */}
      <h2>1. Acceptance of Terms</h2>
      <p>
        By accessing or using {BRAND.name} (the &quot;Service&quot;), operated by{' '}
        {BRAND.companyEntity} (&quot;we&quot;, &quot;us&quot;, or &quot;our&quot;), you agree to
        be bound by these Terms of Service (&quot;Terms&quot;). If you do not agree, do not use
        the Service.
      </p>

      {/* 2. Account */}
      <h2>2. Account</h2>
      <p>
        You must provide accurate information when creating an account. You are responsible for
        maintaining the confidentiality of your credentials and for all activity under your
        account. Notify us immediately at{' '}
        <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> if you suspect
        unauthorised access.
      </p>
      <p>
        You must be at least 16 years old to create an account. Accounts may not be shared or
        transferred without our prior written consent.
      </p>

      {/* 3. Acceptable Use */}
      <h2>3. Acceptable Use</h2>
      <p>You agree not to use the Service to:</p>
      <ul>
        <li>Upload, process, or distribute malware, viruses, or any malicious software.</li>
        <li>
          Upload, process, or distribute child sexual abuse material (CSAM) or any content that
          exploits minors. Such activity will be reported to the National Center for Missing and
          Exploited Children (NCMEC) and relevant law enforcement.
        </li>
        <li>
          Scrape, crawl, or systematically extract data from the Service by automated means
          without our prior written consent.
        </li>
        <li>
          Violate any applicable law, regulation, or third-party rights, including intellectual
          property rights.
        </li>
        <li>Attempt to gain unauthorised access to any part of the Service or our systems.</li>
        <li>Interfere with or disrupt the integrity or performance of the Service.</li>
      </ul>

      {/* 4. Pricing and Billing */}
      <h2>4. Pricing and Billing</h2>
      <p>
        Paid plans are billed in advance on a monthly or annual basis. All fees are
        non-refundable except where required by applicable law or as stated in our refund policy.
        You may request a refund within 14 days of your initial subscription purchase if you are
        not satisfied.
      </p>
      <p>
        You may cancel your subscription at any time from your account settings. Cancellation
        takes effect at the end of the current billing period; you retain access until then.
        Downgrades to a lower plan take effect at the next billing cycle.
      </p>
      <p>
        We reserve the right to change pricing with 30 days&apos; notice. Continued use of a
        paid plan after the notice period constitutes acceptance of the new pricing.
      </p>

      {/* 5. Intellectual Property */}
      <h2>5. Intellectual Property</h2>
      <p>
        <strong>Your files are your intellectual property.</strong> By uploading files to the
        Service, you grant {BRAND.companyEntity} a limited, non-exclusive, royalty-free licence
        to process your files solely for the purpose of delivering the Service to you. This
        licence terminates when your files are deleted (within 60 minutes of processing).
      </p>
      <p>
        The {BRAND.name} platform, including its software, trademarks, and content (excluding your
        files), is owned by {BRAND.companyEntity} and is protected by intellectual property laws.
        You may not copy, modify, or distribute any part of the platform without our prior written
        consent.
      </p>

      {/* 6. Disclaimers */}
      <h2>6. Disclaimers</h2>
      <p>
        THE SERVICE IS PROVIDED &quot;AS IS&quot; AND &quot;AS AVAILABLE&quot; WITHOUT WARRANTIES
        OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO WARRANTIES OF
        MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT. WE DO NOT
        WARRANT THAT THE SERVICE WILL BE UNINTERRUPTED, ERROR-FREE, OR FREE OF VIRUSES OR OTHER
        HARMFUL COMPONENTS.
      </p>

      {/* 7. Limitation of Liability */}
      <h2>7. Limitation of Liability</h2>
      <p>
        TO THE MAXIMUM EXTENT PERMITTED BY LAW, {BRAND.companyEntity.toUpperCase()} AND ITS
        OFFICERS, DIRECTORS, EMPLOYEES, AND AGENTS WILL NOT BE LIABLE FOR ANY INDIRECT,
        INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR PUNITIVE DAMAGES ARISING FROM YOUR USE OF THE
        SERVICE.
      </p>
      <p>
        OUR TOTAL LIABILITY TO YOU FOR ALL CLAIMS ARISING FROM OR RELATING TO THE SERVICE IS
        LIMITED TO THE GREATER OF (A) THE FEES YOU PAID TO US IN THE TWELVE (12) MONTHS
        PRECEDING THE CLAIM, OR (B) ONE HUNDRED US DOLLARS (USD $100).
      </p>

      {/* 8. Indemnification */}
      <h2>8. Indemnification</h2>
      <p>
        You agree to indemnify, defend, and hold harmless {BRAND.companyEntity} and its
        affiliates from and against any claims, liabilities, damages, losses, and expenses
        (including reasonable legal fees) arising out of or in connection with your use of the
        Service, your violation of these Terms, or your violation of any third-party rights.
      </p>

      {/* 9. Termination */}
      <h2>9. Termination</h2>
      <p>
        We may suspend or terminate your account immediately and without notice if you breach
        these Terms or if we are required to do so by law. Upon termination, your right to use
        the Service ceases, and we will delete your account data in accordance with our{' '}
        <a href="/legal/privacy">Privacy Policy</a>.
      </p>
      <p>
        You may terminate your account at any time from your account settings or by contacting{' '}
        <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>.
      </p>

      {/* 10. Governing Law */}
      <h2>10. Governing Law</h2>
      <p>
        These Terms are governed by and construed in accordance with the laws of the State of
        Delaware, United States, without regard to its conflict-of-law provisions. [Placeholder:
        confirm with counsel based on company HQ jurisdiction.]
      </p>

      {/* 11. Disputes */}
      <h2>11. Dispute Resolution</h2>
      <p>
        Any dispute arising out of or relating to these Terms or the Service will be resolved by
        binding individual arbitration under the rules of the American Arbitration Association
        (AAA), except that either party may seek injunctive relief in a court of competent
        jurisdiction to prevent irreparable harm.
      </p>
      <p>
        <strong>Class action waiver:</strong> You agree that any claims must be brought on an
        individual basis and not as part of any class or representative proceeding.
      </p>
      <p>
        If you are an EU consumer, you may also refer disputes to the EU Online Dispute Resolution
        platform at <a href="https://ec.europa.eu/odr">https://ec.europa.eu/odr</a>.
      </p>

      {/* Contact */}
      <h2>Contact</h2>
      <address className="not-italic">
        {BRAND.companyEntity}
        <br />
        {BRAND.legalAddress}
        <br />
        <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>
      </address>
    </article>
  );
}
