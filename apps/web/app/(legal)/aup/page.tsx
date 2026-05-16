import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Acceptable Use Policy',
  description: `Acceptable Use Policy for ${BRAND.name}. Detailed rules governing permitted and prohibited uses of our PDF processing platform.`,
};

const LAST_UPDATED = '2026-05-16';

export default function AcceptableUsePolicyPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Acceptable Use Policy</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      <p>
        This Acceptable Use Policy (&quot;AUP&quot;) forms part of the{' '}
        <a href="/legal/terms">Terms of Service</a> between you and{' '}
        {BRAND.companyEntity} (&quot;{BRAND.name}&quot;, &quot;we&quot;, &quot;us&quot;). By using
        the Service, you agree to comply with this AUP. Capitalised terms not defined here have the
        meanings given in the Terms of Service.
      </p>

      <nav aria-label="Table of contents">
        <ol className="text-sm">
          <li><a href="#prohibited-content">1. Prohibited Content</a></li>
          <li><a href="#prohibited-conduct">2. Prohibited Conduct</a></li>
          <li><a href="#fair-use">3. Fair-Use Thresholds</a></li>
          <li><a href="#ai-use">4. AI Feature Use</a></li>
          <li><a href="#developer-api">5. Developer API Use</a></li>
          <li><a href="#enforcement">6. Enforcement</a></li>
          <li><a href="#reporting">7. Reporting Violations</a></li>
        </ol>
      </nav>

      <section id="prohibited-content">
        <h2>1. Prohibited Content</h2>
        <p>
          You must not upload, process, generate, store, transmit, or distribute through the Service
          any content that:
        </p>
        <ul>
          <li>
            <strong>Child sexual abuse material (CSAM):</strong> Depicts or describes sexual acts
            involving minors, constitutes child sexual abuse material, or otherwise exploits or
            harms minors. Any such activity will be reported immediately to the National Center for
            Missing and Exploited Children (NCMEC), law enforcement, and relevant national
            authorities. Accounts will be terminated immediately without refund.
          </li>
          <li>
            <strong>Malware and malicious code:</strong> Contains or links to viruses, trojans,
            ransomware, spyware, adware, keyloggers, rootkits, or any other software designed to
            damage, disrupt, or gain unauthorised access to systems or data.
          </li>
          <li>
            <strong>Illegal content:</strong> Violates any applicable local, national, or
            international law, including without limitation: copyright and trademark infringement,
            defamation, fraud, identity theft, export control violations, or facilitation of
            unlawful activities.
          </li>
          <li>
            <strong>Terrorist or extremist content:</strong> Promotes, glorifies, incites, or
            facilitates terrorism, violent extremism, genocide, or mass violence.
          </li>
          <li>
            <strong>Hate speech:</strong> Promotes violence, hatred, discrimination, or harassment
            based on race, ethnicity, national origin, religion, gender, sexual orientation,
            gender identity, disability, age, or other protected characteristics.
          </li>
          <li>
            <strong>Non-consensual intimate imagery (NCII):</strong> Depicts intimate images of
            real people distributed without their consent.
          </li>
          <li>
            <strong>Spam content:</strong> Bulk unsolicited commercial communications for which
            the recipients have not given consent.
          </li>
          <li>
            <strong>Misinformation intended to cause harm:</strong> Knowingly false information
            designed to cause physical harm, financial harm, or to undermine public health,
            elections, or democratic processes.
          </li>
        </ul>
      </section>

      <section id="prohibited-conduct">
        <h2>2. Prohibited Conduct</h2>
        <p>
          You must not, through use of the Service or through any other means related to the Service:
        </p>

        <h3>2.1 System Integrity</h3>
        <ul>
          <li>
            <strong>Unauthorised access:</strong> Attempt to gain unauthorised access to any part
            of the Service, other users&apos; accounts, or our internal systems, databases, or
            infrastructure.
          </li>
          <li>
            <strong>Reverse engineering:</strong> Decompile, disassemble, reverse engineer, or
            otherwise attempt to derive the source code, algorithms, or architecture of any part
            of the Service, except to the extent expressly permitted by applicable law (including
            EU Directive 2009/24/EC on computer programs).
          </li>
          <li>
            <strong>Security probing:</strong> Probe, scan, or test the vulnerability of the
            Service or any related system, or circumvent authentication mechanisms, rate limiting,
            access controls, or encryption — except through our responsible disclosure programme
            ({' '}
            <a href={`https://${BRAND.domain}/security.txt`}>/security.txt</a> for the safe-harbour
            scope).
          </li>
          <li>
            <strong>Service disruption:</strong> Deliberately attempt to overload, disrupt, or
            degrade the performance of the Service (e.g., DDoS, resource exhaustion), or interfere
            with other users&apos; ability to access the Service.
          </li>
          <li>
            <strong>Introduce malware:</strong> Upload, attach, or transmit any code, file, or
            content designed to damage, disable, or intercept data from systems.
          </li>
        </ul>

        <h3>2.2 Automated and Bulk Use</h3>
        <ul>
          <li>
            <strong>Scraping:</strong> Use automated scripts, bots, spiders, crawlers, or other
            automated tools to scrape, extract, or copy data from the Service, including user
            interface content, metadata, or API responses, without our prior written consent.
          </li>
          <li>
            <strong>Bulk uploads exceeding fair-use thresholds:</strong> Use the Service in an
            automated manner that exceeds the fair-use thresholds defined in Section 3, unless
            you hold a valid API plan that explicitly permits programmatic access.
          </li>
          <li>
            <strong>Unofficial API use:</strong> Use the web interface (browser-based application)
            as an unofficial API by reverse-engineering or intercepting API calls, bypassing
            intended access methods. Use our official public API (where available) instead.
          </li>
        </ul>

        <h3>2.3 Resale and Commercial Restrictions</h3>
        <ul>
          <li>
            <strong>Unauthorised resale:</strong> Resell, sublicense, time-share, or provide the
            Service to third parties as a white-label or embedded product without a separate
            written commercial API or reseller agreement with {BRAND.companyEntity}.
          </li>
          <li>
            <strong>Competitive intelligence:</strong> Use the Service to benchmark or evaluate
            a competing product without our prior written consent.
          </li>
        </ul>

        <h3>2.4 Deception and Abuse</h3>
        <ul>
          <li>
            <strong>Impersonation:</strong> Impersonate {BRAND.name}, our staff, or any other
            person or entity, including by creating fake support communications, phishing pages,
            or fraudulent receipts.
          </li>
          <li>
            <strong>Circumventing restrictions:</strong> Create additional accounts to circumvent
            free-tier limits, suspensions, or other restrictions applied to your account.
          </li>
          <li>
            <strong>False information:</strong> Provide false, misleading, or fraudulent
            information during registration, support interactions, or payment processing.
          </li>
        </ul>

        <h3>2.5 Data Harvesting</h3>
        <ul>
          <li>
            Collect, harvest, or mine personal data about other users of the Service by any means,
            including through the Service API.
          </li>
          <li>
            Use the AI features to generate personalised profiles or dossiers on private
            individuals without their consent.
          </li>
        </ul>
      </section>

      <section id="fair-use">
        <h2>3. Fair-Use Thresholds</h2>
        <p>
          The Service is designed for human-driven, interactive use. The following indicative
          thresholds apply per account per rolling 24-hour period for free-tier accounts:
        </p>
        <ul>
          <li>
            <strong>Free tier:</strong> Maximum [TODO: e.g., 10 PDF operations] per day; maximum
            [TODO: e.g., 50 MB] aggregate file size per day. [Business decision required —
            confirm limits before launch.]
          </li>
          <li>
            <strong>Pro tier:</strong> Higher limits as specified on the pricing page. Automated
            use requires our API plan.
          </li>
          <li>
            <strong>API plan:</strong> Rate limits and quotas are defined per-plan in the API
            documentation. Programmatic/automated access is only permitted on an API plan.
          </li>
        </ul>
        <p>
          We reserve the right to throttle, suspend, or terminate accounts that exceed fair-use
          thresholds or use the Service in ways that degrade service quality for other users. We
          will endeavour to give advance notice before suspension except in cases of severe abuse.
        </p>
      </section>

      <section id="ai-use">
        <h2>4. AI Feature Use</h2>
        <p>
          Our AI features (PDF summarisation, document Q&amp;A, data extraction) are powered by
          third-party language model APIs (Anthropic, OpenAI). Additional constraints apply:
        </p>
        <ul>
          <li>
            You must not use AI features to process documents containing personal data of third
            parties without a valid legal basis under applicable data protection law (e.g., GDPR).
          </li>
          <li>
            You must not attempt to extract training data, system prompts, or internal model
            information from the AI features (prompt injection attacks).
          </li>
          <li>
            You must not use AI-generated outputs to deceive others into believing they are
            human-generated expert opinion without appropriate disclosure.
          </li>
          <li>
            You must not use AI features to generate illegal content, including deepfakes,
            fraudulent documents, or content that facilitates crime.
          </li>
          <li>
            AI-generated outputs may be inaccurate or incomplete. You are responsible for
            verifying outputs before relying on them for any purpose. We are not liable for
            errors in AI-generated content.
          </li>
        </ul>
        <p>
          Use of AI features is also subject to the acceptable use policies of our AI
          subprocessors:{' '}
          <a href="https://www.anthropic.com/legal/aup" target="_blank" rel="noopener noreferrer">
            Anthropic AUP
          </a>{' '}
          and{' '}
          <a href="https://openai.com/policies/usage-policies" target="_blank" rel="noopener noreferrer">
            OpenAI Usage Policies
          </a>
          .
        </p>
      </section>

      <section id="developer-api">
        <h2>5. Developer API Use</h2>
        <p>
          If you access the {BRAND.name} API with an API key, additional obligations apply:
        </p>
        <ul>
          <li>
            You must keep your API keys confidential. Do not embed them in client-side code or
            public repositories.
          </li>
          <li>
            You are responsible for all API activity under your key, including activity by
            applications or services you build.
          </li>
          <li>
            Applications you build using the API must also comply with this AUP with respect to
            the content they submit to the Service.
          </li>
          <li>
            You must implement reasonable rate limiting and retry logic in your integration. Do not
            retry failed requests in tight loops.
          </li>
          <li>
            You must not attempt to circumvent API rate limits or quotas, or use multiple API keys
            to aggregate quota beyond your plan entitlement.
          </li>
        </ul>
      </section>

      <section id="enforcement">
        <h2>6. Enforcement</h2>
        <p>
          We reserve the right to investigate suspected violations of this AUP. If we determine
          that a violation has occurred, we may take any of the following actions at our discretion:
        </p>
        <ul>
          <li>Issue a warning to the account holder.</li>
          <li>Temporarily suspend access to specific features or the Service.</li>
          <li>Permanently terminate the account, without refund.</li>
          <li>
            Remove or disable access to specific content that violates this AUP.
          </li>
          <li>
            Report the activity to law enforcement and regulatory authorities where required by
            law or where we reasonably believe doing so is necessary to protect the safety of users
            or the public.
          </li>
          <li>
            Cooperate with law enforcement investigations, including disclosing account data as
            required by valid legal process.
          </li>
        </ul>
        <p>
          For most violations (other than CSAM, malware, fraud, and severe security incidents),
          we will attempt to give you notice and an opportunity to cure the violation before
          taking action, unless doing so would cause harm to others.
        </p>
      </section>

      <section id="reporting">
        <h2>7. Reporting Violations</h2>
        <p>
          If you encounter content or conduct on the Service that you believe violates this AUP,
          please report it:
        </p>
        <ul>
          <li>
            <strong>Abuse reports:</strong>{' '}
            <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> — Subject:
            &quot;AUP Violation Report&quot;
          </li>
          <li>
            <strong>Security issues:</strong>{' '}
            <a href={`mailto:${BRAND.securityEmail}`}>{BRAND.securityEmail}</a> — or see{' '}
            <a href="/legal/security#disclosure">responsible disclosure</a>
          </li>
          <li>
            <strong>CSAM:</strong> Also report directly to NCMEC CyberTipline:{' '}
            <a
              href="https://www.missingkids.org/gethelpnow/cybertipline"
              target="_blank"
              rel="noopener noreferrer"
            >
              missingkids.org/gethelpnow/cybertipline
            </a>
          </li>
        </ul>
        <p>We take all reports seriously and will investigate promptly.</p>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
