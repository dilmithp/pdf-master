import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Privacy Policy',
  description: `Privacy Policy for ${BRAND.name}. Learn how we collect, use, and protect your personal data under GDPR, CCPA/CPRA, and other applicable laws.`,
};

const LAST_UPDATED = '2026-05-16';

interface RetentionRow {
  dataType: string;
  period: string;
  reason: string;
}

interface LegalBasisRow {
  activity: string;
  dataCategories: string;
  legalBasis: string;
  gdprRef: string;
}

const LEGAL_BASES: LegalBasisRow[] = [
  {
    activity: 'Account creation and management',
    dataCategories: 'Email, name, password hash',
    legalBasis: 'Contract',
    gdprRef: 'Art. 6(1)(b)',
  },
  {
    activity: 'Payment processing and billing',
    dataCategories: 'Billing name, address, last 4 card digits',
    legalBasis: 'Contract + Legal obligation',
    gdprRef: 'Art. 6(1)(b), 6(1)(c)',
  },
  {
    activity: 'PDF file processing (upload and transform)',
    dataCategories: 'File content (transient — deleted within 60 min)',
    legalBasis: 'Contract (transient processing)',
    gdprRef: 'Art. 6(1)(b)',
  },
  {
    activity: 'AI features (summarise, chat, extract)',
    dataCategories: 'Document text passed to AI subprocessors',
    legalBasis: 'Contract',
    gdprRef: 'Art. 6(1)(b)',
  },
  {
    activity: 'Analytics and product improvement',
    dataCategories: 'Usage events, page views, feature interactions',
    legalBasis: 'Consent',
    gdprRef: 'Art. 6(1)(a)',
  },
  {
    activity: 'Security, fraud prevention, and anti-abuse',
    dataCategories: 'IP address, request logs, error events',
    legalBasis: 'Legitimate interest',
    gdprRef: 'Art. 6(1)(f)',
  },
  {
    activity: 'Transactional communications (receipts, alerts)',
    dataCategories: 'Email address',
    legalBasis: 'Contract',
    gdprRef: 'Art. 6(1)(b)',
  },
  {
    activity: 'Marketing communications (newsletters)',
    dataCategories: 'Email address, name',
    legalBasis: 'Consent',
    gdprRef: 'Art. 6(1)(a)',
  },
  {
    activity: 'Tax and accounting record-keeping',
    dataCategories: 'Invoice data, payment amounts',
    legalBasis: 'Legal obligation',
    gdprRef: 'Art. 6(1)(c)',
  },
];

const RETENTION: RetentionRow[] = [
  {
    dataType: 'Account data (email, name, settings)',
    period: 'Until account deletion + 30-day soft-delete window',
    reason: 'Contract performance; allows account recovery during grace period',
  },
  {
    dataType: 'Uploaded files (PDFs and processed outputs)',
    period: 'Maximum 60 minutes from upload',
    reason: 'Privacy-by-design; files auto-deleted by S3 lifecycle rule',
  },
  {
    dataType: 'AI embeddings (pgvector)',
    period: 'Tied to parent document; deleted on document expiry or account deletion',
    reason: 'Embeddings have no independent utility after the document is gone',
  },
  {
    dataType: 'Payment and billing records',
    period: '7 years from transaction date',
    reason: 'Legal obligation — tax, accounting, and consumer protection laws',
  },
  {
    dataType: 'Audit logs (access, admin actions)',
    period: '1 year',
    reason: 'Security monitoring and incident investigation',
  },
  {
    dataType: 'Server/request logs (IP, user-agent)',
    period: '90 days',
    reason: 'Security and debugging; rolling deletion',
  },
  {
    dataType: 'Deleted account residual data',
    period: '30-day soft-delete window, then purged',
    reason: 'Allows recovery within grace period; purged after',
  },
];

export default function PrivacyPolicyPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Privacy Policy</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      <nav aria-label="Table of contents">
        <ol className="text-sm">
          <li><a href="#who-we-are">1. Who We Are</a></li>
          <li><a href="#data-we-collect">2. Personal Data We Collect</a></li>
          <li><a href="#legal-bases">3. Lawful Bases per Processing Activity</a></li>
          <li><a href="#ai-processing">4. AI Processing Disclosure</a></li>
          <li><a href="#retention">5. Data Retention</a></li>
          <li><a href="#international-transfers">6. International Data Transfers</a></li>
          <li><a href="#subprocessors">7. Subprocessors</a></li>
          <li><a href="#your-rights">8. Your Rights (GDPR)</a></li>
          <li><a href="#ccpa">9. California Residents (CCPA/CPRA)</a></li>
          <li><a href="#children">10. Children&apos;s Data</a></li>
          <li><a href="#automated-decisions">11. Automated Decision-Making</a></li>
          <li><a href="#cookies">12. Cookies</a></li>
          <li><a href="#changes">13. Changes to This Policy</a></li>
          <li><a href="#contact">14. Contact and Supervisory Authorities</a></li>
        </ol>
      </nav>

      {/* 1. Who We Are */}
      <section id="who-we-are">
        <h2>1. Who We Are</h2>
        <p>
          {BRAND.companyEntity} (&quot;{BRAND.name}&quot;, &quot;we&quot;, &quot;us&quot;, or
          &quot;our&quot;) is the <strong>data controller</strong> for personal data processed
          through our platform, accessible at{' '}
          <a href={`https://${BRAND.domain}`}>{BRAND.domain}</a>.
        </p>
        <p>
          <strong>Registered address:</strong> {BRAND.legalAddress}
        </p>
        <p>
          <strong>Data Protection Officer (DPO):</strong>{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>
        </p>
        <p>
          <strong>EU Representative (Art. 27 GDPR):</strong> [TODO — Required if {BRAND.name} is
          not established in the EU. Appoint via a service such as EDPO (edpo.com) or VeraSafe
          (verasafe.com) before public launch in the EU/EEA.]
        </p>
        <p>
          <strong>UK Representative:</strong> [TODO — Required if offering services to UK residents
          without a UK establishment. Appoint per UK GDPR Art. 27.]
        </p>
      </section>

      {/* 2. Personal Data We Collect */}
      <section id="data-we-collect">
        <h2>2. Personal Data We Collect</h2>
        <p>We collect the following categories of personal data:</p>
        <ul>
          <li>
            <strong>Account data:</strong> Email address, display name, and password hash (hashed
            via Clerk — we never see the plaintext). Profile picture if you connect via OAuth
            (Google, GitHub).
          </li>
          <li>
            <strong>Payment data:</strong> Billing name, billing address, and the last four digits
            of your payment card. Full card numbers, CVVs, and bank details are processed
            exclusively by Stripe and are never stored on our servers.
          </li>
          <li>
            <strong>Uploaded files:</strong> PDF and other documents you upload for processing.
            These are encrypted at rest (AES-256, AWS KMS) and automatically and irreversibly
            deleted within <strong>60 minutes</strong> of upload, regardless of account status.
            We do not read, index, or use your file content for any purpose other than executing
            the specific operation you request.
          </li>
          <li>
            <strong>AI feature inputs:</strong> When you use AI features (summarise, chat, extract),
            the text content of your document is extracted and sent to our AI subprocessors
            (Anthropic, OpenAI) solely to fulfil your request. See Section 4.
          </li>
          <li>
            <strong>Usage data:</strong> IP address, browser type and version, operating system,
            pages visited, features used, time on page, and error/crash logs. Used for security,
            debugging, and (with your consent) analytics.
          </li>
          <li>
            <strong>Cookie data:</strong> Session identifiers and, where you have given consent,
            analytics identifiers. See our{' '}
            <a href="/legal/cookies">Cookie Policy</a> and Section 12 below.
          </li>
          <li>
            <strong>Communications data:</strong> Messages you send to our support team, including
            email content and any attachments.
          </li>
        </ul>
      </section>

      {/* 3. Legal Bases Table */}
      <section id="legal-bases">
        <h2>3. Lawful Bases per Processing Activity</h2>
        <p>
          We process your personal data only when we have a valid lawful basis under GDPR Article 6.
          The table below maps each processing activity to its lawful basis.
        </p>
        <div className="not-prose overflow-x-auto">
          <table className="w-full border-collapse text-sm">
            <caption className="mb-2 text-left text-xs text-neutral-500">
              GDPR Article 6 lawful bases per processing activity
            </caption>
            <thead>
              <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
                <th className="py-2 pr-4 font-semibold">Processing Activity</th>
                <th className="py-2 pr-4 font-semibold">Data Categories</th>
                <th className="py-2 pr-4 font-semibold">Lawful Basis</th>
                <th className="py-2 font-semibold">GDPR Reference</th>
              </tr>
            </thead>
            <tbody>
              {LEGAL_BASES.map((row) => (
                <tr
                  key={row.activity}
                  className="border-b border-neutral-100 dark:border-neutral-800"
                >
                  <td className="py-2 pr-4 font-medium">{row.activity}</td>
                  <td className="py-2 pr-4 text-neutral-600 dark:text-neutral-400">
                    {row.dataCategories}
                  </td>
                  <td className="py-2 pr-4">{row.legalBasis}</td>
                  <td className="py-2 font-mono text-xs">{row.gdprRef}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <p className="mt-4">
          Where we rely on <strong>legitimate interest</strong> (Art. 6(1)(f)), we have conducted a
          balancing test and concluded our interests in security and fraud prevention are not
          overridden by your rights, given the minimal data involved and the direct benefit to you.
          You may request a copy of our legitimate interest assessment by emailing{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
        </p>
        <p>
          Where we rely on <strong>consent</strong> (Art. 6(1)(a)), you may withdraw that consent at
          any time without affecting the lawfulness of prior processing. Withdrawal of consent for
          analytics cookies does not affect your ability to use the service.
        </p>
      </section>

      {/* 4. AI Processing Disclosure */}
      <section id="ai-processing">
        <h2>4. AI Processing Disclosure</h2>
        <p>
          When you use our AI-powered features (PDF summarisation, document chat, data extraction,
          or similar), the following occurs:
        </p>
        <ol>
          <li>
            We extract the text content of your document on our servers within a sandboxed,
            network-isolated worker container.
          </li>
          <li>
            That text is transmitted over TLS 1.3 to{' '}
            <strong>Anthropic</strong> (Claude API) for language model completions, and/or to{' '}
            <strong>OpenAI</strong> (Embeddings API) to generate vector embeddings for semantic
            search.
          </li>
          <li>
            Both Anthropic and OpenAI act as <strong>data subprocessors</strong> under their
            respective Data Processing Addendums (DPAs). They are contractually prohibited from
            using your data to train their models under these agreements. You can verify at:{' '}
            <a
              href="https://www.anthropic.com/legal/aup"
              target="_blank"
              rel="noopener noreferrer"
            >
              anthropic.com/legal/aup
            </a>{' '}
            and{' '}
            <a
              href="https://openai.com/policies/data-processing-addendum"
              target="_blank"
              rel="noopener noreferrer"
            >
              openai.com/policies/data-processing-addendum
            </a>
            .
          </li>
          <li>
            Vector embeddings are stored in our <strong>pgvector</strong> database (hosted on AWS
            RDS, encrypted at rest with AES-256) to enable semantic search within your session.
            Embeddings are <strong>deleted</strong> when the parent document expires (within 60
            minutes) or when you delete your account — whichever comes first.
          </li>
          <li>
            We do not use your document content or AI outputs to train our own models, to improve
            third-party models, or for any purpose other than delivering your requested result.
          </li>
        </ol>
        <p>
          <strong>AI feature opt-out:</strong> If you do not wish your document text to be sent to
          AI subprocessors, simply do not use AI features. Core PDF operations (merge, split,
          compress, convert, sign) do not involve any AI subprocessors.
        </p>
      </section>

      {/* 5. Retention Table */}
      <section id="retention">
        <h2>5. Data Retention</h2>
        <p>
          We retain personal data for the minimum period necessary for the purpose for which it was
          collected. The table below sets out our retention periods per data type.
        </p>
        <div className="not-prose overflow-x-auto">
          <table className="w-full border-collapse text-sm">
            <caption className="mb-2 text-left text-xs text-neutral-500">
              Retention periods per data category
            </caption>
            <thead>
              <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
                <th className="py-2 pr-4 font-semibold">Data Type</th>
                <th className="py-2 pr-4 font-semibold">Retention Period</th>
                <th className="py-2 font-semibold">Reason</th>
              </tr>
            </thead>
            <tbody>
              {RETENTION.map((row) => (
                <tr
                  key={row.dataType}
                  className="border-b border-neutral-100 dark:border-neutral-800"
                >
                  <td className="py-2 pr-4 font-medium">{row.dataType}</td>
                  <td className="py-2 pr-4">{row.period}</td>
                  <td className="py-2 text-neutral-600 dark:text-neutral-400">{row.reason}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <p className="mt-4">
          When a retention period expires, data is either deleted or anonymised such that it can no
          longer be linked to an identified or identifiable individual.
        </p>
      </section>

      {/* 6. International Transfers */}
      <section id="international-transfers">
        <h2>6. International Data Transfers</h2>
        <p>
          We operate primarily on Amazon Web Services (AWS) in two regions:{' '}
          <strong>us-east-1</strong> (Virginia, USA) and <strong>eu-west-1</strong> (Ireland, EU).
          Users in the EU/EEA have their primary data processed in <strong>eu-west-1</strong> by
          default.
        </p>
        <p>
          However, certain subprocessors (Anthropic, OpenAI, Stripe, Clerk, Postmark, Sentry) are
          based in the <strong>United States</strong>. Transfers of personal data from the EU/EEA
          and UK to these US-based processors rely on the following transfer mechanisms:
        </p>
        <ul>
          <li>
            <strong>EU Standard Contractual Clauses (SCCs)</strong> — approved by the European
            Commission under Decision 2021/914/EU (the &quot;2021 SCCs&quot;). All US-based
            subprocessors operate under SCCs incorporating the controller-to-processor or
            processor-to-processor modules as applicable.
          </li>
          <li>
            <strong>UK International Data Transfer Agreement (IDTA) Addendum</strong> — the UK
            equivalent, effective 21 March 2022, incorporated into subprocessor agreements for
            transfers from the UK. Where a subprocessor has signed the EU SCCs, we rely on the UK
            IDTA Addendum to extend coverage to UK transfers.
          </li>
          <li>
            <strong>EU-US Data Privacy Framework (DPF)</strong> — where a subprocessor is
            certified under the DPF (successor to Privacy Shield, recognised by the European
            Commission in Decision 2023/1795), we additionally rely on DPF certification as a
            supplementary transfer mechanism.
          </li>
        </ul>
        <p>
          Cloudflare operates a global edge network; edge nodes in the EU process EU requests
          locally where technically feasible. For a full list of subprocessors with their locations
          and DPA links, see our{' '}
          <a href="/legal/subprocessors">Subprocessors page</a>.
        </p>
        <p>
          You may request copies of the applicable SCCs and transfer impact assessments by emailing{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
        </p>
      </section>

      {/* 7. Subprocessors */}
      <section id="subprocessors">
        <h2>7. Subprocessors</h2>
        <p>
          We engage trusted third-party subprocessors under Data Processing Agreements. They are
          permitted to process personal data only as instructed by us and for the purposes described.
          A complete and current list — including each subprocessor&apos;s name, purpose, data
          categories processed, location, and DPA link — is available at{' '}
          <a href="/legal/subprocessors">/legal/subprocessors</a>.
        </p>
        <p>
          We will give you <strong>30 days&apos; notice</strong> (via in-app banner and email)
          before adding any new subprocessor that processes personal data. You may object to such
          changes by terminating your subscription before the change takes effect.
        </p>
      </section>

      {/* 8. Your Rights */}
      <section id="your-rights">
        <h2>8. Your Rights Under GDPR</h2>
        <p>
          If you are located in the EU, EEA, or UK, you have the following rights under GDPR (and
          equivalent UK GDPR):
        </p>
        <ul>
          <li>
            <strong>Right of access (Art. 15):</strong> Request a copy of the personal data we hold
            about you, including the categories, purposes, recipients, and retention periods.
          </li>
          <li>
            <strong>Right to rectification (Art. 16):</strong> Correct inaccurate or incomplete
            personal data. You can update most account data directly in Settings.
          </li>
          <li>
            <strong>Right to erasure / &quot;right to be forgotten&quot; (Art. 17):</strong>{' '}
            Request deletion of your personal data where it is no longer necessary, where consent
            is withdrawn, or where processing is unlawful. Note: we cannot erase data we are
            legally required to retain (e.g., billing records for 7 years).
          </li>
          <li>
            <strong>Right to restriction (Art. 18):</strong> Request that we restrict processing
            in certain circumstances (e.g., while accuracy is contested, or where processing is
            unlawful but you prefer restriction over erasure).
          </li>
          <li>
            <strong>Right to data portability (Art. 20):</strong> Receive your personal data in a
            structured, commonly used, machine-readable format (JSON/CSV) and transmit it to
            another controller. Applies to data processed on the basis of contract or consent.
          </li>
          <li>
            <strong>Right to object (Art. 21):</strong> Object to processing based on legitimate
            interests, including profiling; and to object unconditionally to processing for direct
            marketing purposes. We will stop processing on objection unless we demonstrate
            compelling legitimate grounds.
          </li>
          <li>
            <strong>Right not to be subject to solely automated decisions (Art. 22):</strong> See
            Section 11.
          </li>
          <li>
            <strong>Right to withdraw consent:</strong> Where processing is based on consent, you
            may withdraw at any time without affecting prior processing. Use your account cookie
            settings or email us.
          </li>
        </ul>

        <h3>How to Exercise Your Rights</h3>
        <ol>
          <li>
            Email <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a> with your request,
            clearly stating which right you wish to exercise and your account email address.
          </li>
          <li>
            We may need to verify your identity before acting on your request. We will ask for
            information sufficient to confirm your identity — typically your account email and a
            verification step. We will not ask for more information than necessary.
          </li>
          <li>
            <strong>Response time:</strong> We will respond within <strong>1 month</strong> of
            receiving your verified request (GDPR Art. 12(3)). For complex or numerous requests, we
            may extend by a further 2 months, but will notify you within the first month with the
            reason for extension.
          </li>
          <li>
            <strong>Cost:</strong> Requests are free of charge for the first request in a 12-month
            period. For manifestly unfounded or excessive repeat requests, we may charge a
            reasonable administrative fee or refuse to act (GDPR Art. 12(5)).
          </li>
        </ol>

        <h3>Right to Lodge a Complaint</h3>
        <p>
          If you believe we have not handled your personal data in accordance with applicable law,
          you have the right to lodge a complaint with your local data protection supervisory
          authority:
        </p>
        <ul>
          <li>
            <strong>EU:</strong> Find your national DPA at{' '}
            <a
              href="https://edpb.europa.eu/about-edpb/board/members_en"
              target="_blank"
              rel="noopener noreferrer"
            >
              edpb.europa.eu/about-edpb/board/members_en
            </a>
          </li>
          <li>
            <strong>UK:</strong> Information Commissioner&apos;s Office (ICO) —{' '}
            <a href="https://ico.org.uk" target="_blank" rel="noopener noreferrer">
              ico.org.uk
            </a>
          </li>
          <li>
            <strong>California, USA:</strong> California Attorney General —{' '}
            <a
              href="https://oag.ca.gov/privacy"
              target="_blank"
              rel="noopener noreferrer"
            >
              oag.ca.gov/privacy
            </a>
          </li>
        </ul>
        <p>
          We would appreciate the opportunity to address your concern before you contact a
          supervisory authority, but filing a complaint is always your right.
        </p>
      </section>

      {/* 9. CCPA / CPRA */}
      <section id="ccpa">
        <h2>9. California Residents — CCPA/CPRA Rights</h2>
        <p>
          If you are a California resident, the California Consumer Privacy Act (CCPA), as amended
          by the California Privacy Rights Act (CPRA), grants you additional rights. This section
          supplements our GDPR disclosures above.
        </p>

        <h3>Categories of Personal Information Collected (Cal. Civ. Code §1798.140(v))</h3>
        <ul>
          <li>
            <strong>Identifiers</strong> — name, email address, IP address, account ID.
          </li>
          <li>
            <strong>Commercial information</strong> — subscription plan, billing history,
            transaction records.
          </li>
          <li>
            <strong>Internet or other electronic network activity</strong> — browsing history on
            our site, feature usage, click events.
          </li>
          <li>
            <strong>Professional or employment-related information</strong> — only if voluntarily
            provided (e.g., company name on the billing form).
          </li>
          <li>
            <strong>Inferences drawn from the above</strong> — aggregate product analytics. We do
            not build individual profiles for advertising.
          </li>
        </ul>
        <p>
          We do not collect biometric information, precise geolocation, health data, or financial
          information beyond payment card last-4-digits.
        </p>

        <h3>Sale and Sharing of Personal Information</h3>
        <p>
          <strong>
            We do not sell your personal information. We do not share your personal information for
            cross-context behavioural advertising.
          </strong>{' '}
          The CCPA &quot;Do Not Sell or Share My Personal Information&quot; right is therefore
          satisfied by default. We do not engage in targeted advertising based on personal
          information obtained from other services.
        </p>

        <h3>Your CCPA/CPRA Rights</h3>
        <ul>
          <li>
            <strong>Right to know (§1798.100):</strong> Request disclosure of the categories and
            specific pieces of personal information we have collected about you, the sources, the
            business purposes, and the categories of third parties we share it with.
          </li>
          <li>
            <strong>Right to delete (§1798.105):</strong> Request deletion of personal information
            we have collected, subject to certain exceptions (e.g., legal obligations, security,
            debugging).
          </li>
          <li>
            <strong>Right to correct (§1798.106):</strong> Request correction of inaccurate
            personal information.
          </li>
          <li>
            <strong>Right to opt-out of sale/sharing (§1798.120):</strong> Not applicable — we do
            not sell or share personal information.
          </li>
          <li>
            <strong>Right to limit use of sensitive personal information (§1798.121):</strong> We
            collect sensitive PI only to the extent necessary to provide the service (e.g., payment
            card last-4 for receipts). You may request limitation to the extent CPRA permits.
          </li>
          <li>
            <strong>Right to non-discrimination (§1798.125):</strong> We will not discriminate
            against you for exercising your CCPA/CPRA rights. Exercising these rights will not
            result in denial of service, different prices, or lower quality of service.
          </li>
        </ul>

        <h3>How to Submit a CCPA/CPRA Request</h3>
        <p>
          Email <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a> with subject line
          &quot;CCPA Request&quot; and specify the right you wish to exercise. We will verify your
          identity and respond within 45 days (extendable by a further 45 days with notice).
        </p>
        <p>
          California residents may also use a browser signal such as the{' '}
          <strong>Global Privacy Control (GPC)</strong>. When we detect a valid GPC signal, we
          treat it as an opt-out of sale/sharing (which, as noted above, we do not engage in, but
          we honour the signal by not enabling any optional tracking cookies for that session).
        </p>
      </section>

      {/* 10. Children */}
      <section id="children">
        <h2>10. Children&apos;s Data</h2>
        <p>
          Our services are not directed at children and we do not knowingly collect personal data
          from children:
        </p>
        <ul>
          <li>
            <strong>EU/EEA/UK:</strong> Under 16 years of age (per GDPR Art. 8 — the minimum age
            threshold adopted under EU law; individual member states may set a lower minimum of 13).
          </li>
          <li>
            <strong>United States:</strong> Under 13 years of age (per COPPA — Children&apos;s
            Online Privacy Protection Act).
          </li>
        </ul>
        <p>
          If we become aware that a child under the applicable age threshold has created an account
          or provided us with personal data, we will delete that data promptly and close the account.
          If you believe a child has provided us with their personal data, please contact{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a> immediately.
        </p>
      </section>

      {/* 11. Automated Decisions */}
      <section id="automated-decisions">
        <h2>11. Automated Decision-Making and Profiling</h2>
        <p>
          We do <strong>not</strong> engage in automated decision-making that produces legal effects
          concerning you or similarly significant effects, within the meaning of GDPR Article 22.
          Decisions about your account (e.g., subscription plan features, access levels) are not
          made solely by automated means without human involvement.
        </p>
        <p>
          Our fraud and abuse detection systems may flag accounts for human review, but no account
          action (suspension, termination) is taken without human involvement in the final decision.
        </p>
        <p>
          [TODO: If automated content moderation is implemented (e.g., automated rejection of
          uploads based on content policy), this section must be updated to reflect GDPR Art. 22
          requirements including the right to human review, to express your point of view, and to
          contest the decision.]
        </p>
      </section>

      {/* 12. Cookies */}
      <section id="cookies">
        <h2>12. Cookies</h2>
        <p>
          We use essential cookies for authentication and session management, and optional analytics
          and marketing cookies subject to your explicit consent. You will be presented with a
          consent banner on your first visit and may change your preferences at any time by
          clicking &quot;Manage cookies&quot; in the site footer.
        </p>
        <p>
          For a full inventory of cookies we set — including cookie name, provider, purpose,
          duration, and type — see our <a href="/legal/cookies">Cookie Policy</a>.
        </p>
      </section>

      {/* 13. Changes */}
      <section id="changes">
        <h2>13. Changes to This Policy</h2>
        <p>We may update this Privacy Policy from time to time. When we make changes:</p>
        <ul>
          <li>
            <strong>Non-material changes</strong> (e.g., clarifications, formatting, adding new
            subprocessors with 30-day advance notice given separately): we will update the
            &quot;Last updated&quot; date at the top of this page.
          </li>
          <li>
            <strong>Material changes</strong> (e.g., new purposes for processing, new categories of
            personal data, changes to international transfer mechanisms): we will notify you via{' '}
            <strong>in-app banner and email</strong> sent to your registered email address at least{' '}
            <strong>30 days before</strong> the change takes effect. The notification will describe
            the nature of the change and, where consent is required, ask for your renewed consent.
          </li>
        </ul>
        <p>
          If you do not agree with material changes, you may close your account before the effective
          date. Continued use of the service after the effective date constitutes acceptance of the
          updated policy, to the extent permitted by law.
        </p>
      </section>

      {/* 14. Contact */}
      <section id="contact">
        <h2>14. Contact</h2>
        <p>For privacy-related enquiries, to exercise your rights, or for any other question about
          this policy, contact our Data Protection Officer:</p>
        <address className="not-italic">
          <strong>{BRAND.companyEntity}</strong>
          <br />
          Attn: Data Protection Officer
          <br />
          {BRAND.legalAddress}
          <br />
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>
        </address>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
