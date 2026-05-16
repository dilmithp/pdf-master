import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Terms of Service',
  description: `Terms of Service for ${BRAND.name}. Read our terms for using our PDF productivity platform, including acceptable use, billing, dispute resolution, and EU consumer rights.`,
};

const LAST_UPDATED = '2026-05-16';

export default function TermsOfServicePage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Terms of Service</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      <nav aria-label="Table of contents">
        <ol className="text-sm">
          <li><a href="#acceptance">1. Acceptance of Terms</a></li>
          <li><a href="#account">2. Account Registration</a></li>
          <li><a href="#aup">3. Acceptable Use Policy</a></li>
          <li><a href="#ip">4. Intellectual Property</a></li>
          <li><a href="#billing">5. Pricing, Billing, and Refunds</a></li>
          <li><a href="#sla">6. Service Levels and Availability</a></li>
          <li><a href="#dmca">7. Copyright / DMCA and EU Notice-and-Action</a></li>
          <li><a href="#suspension">8. Account Suspension and Termination</a></li>
          <li><a href="#disclaimers">9. Disclaimers and Warranties</a></li>
          <li><a href="#liability">10. Limitation of Liability</a></li>
          <li><a href="#indemnification">11. Indemnification (Mutual)</a></li>
          <li><a href="#eu-consumer">12. EU / EEA Consumer Rights</a></li>
          <li><a href="#arbitration">13. Dispute Resolution and Arbitration</a></li>
          <li><a href="#force-majeure">14. Force Majeure</a></li>
          <li><a href="#general">15. General Provisions</a></li>
          <li><a href="#contact">Contact</a></li>
        </ol>
      </nav>

      {/* 1. Acceptance */}
      <section id="acceptance">
        <h2>1. Acceptance of Terms</h2>
        <p>
          By accessing or using the {BRAND.name} platform (the &quot;Service&quot;), operated by{' '}
          {BRAND.companyEntity} (&quot;we&quot;, &quot;us&quot;, or &quot;our&quot;) at{' '}
          <a href={`https://${BRAND.domain}`}>{BRAND.domain}</a>, you agree to be bound by these
          Terms of Service (&quot;Terms&quot;) and our{' '}
          <a href="/legal/privacy">Privacy Policy</a>. If you do not agree, you must not access or
          use the Service.
        </p>
        <p>
          If you are accepting these Terms on behalf of an organisation, you represent and warrant
          that you have authority to bind that organisation, and &quot;you&quot; in these Terms
          refers to that organisation.
        </p>
        <p>
          These Terms constitute a legally binding agreement. We encourage you to read them carefully.
          If you have questions, contact us at{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> before using the Service.
        </p>
      </section>

      {/* 2. Account */}
      <section id="account">
        <h2>2. Account Registration</h2>
        <p>
          You must provide accurate, current, and complete information when registering for an
          account and keep it up to date. You are responsible for maintaining the confidentiality
          of your credentials and for all activity that occurs under your account.
        </p>
        <p>
          You must notify us <strong>immediately</strong> at{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> if you suspect
          unauthorised access to your account. We are not liable for any loss arising from your
          failure to keep credentials secure.
        </p>
        <p>
          You must be at least <strong>16 years old</strong> (EU/EEA/UK) or{' '}
          <strong>13 years old</strong> (USA) to create an account. Accounts may not be shared with
          or transferred to another person without our prior written consent.
        </p>
        <p>
          We reserve the right to refuse registration or to cancel accounts at our discretion,
          including where we suspect fraud, misrepresentation, or violation of these Terms.
        </p>
      </section>

      {/* 3. AUP */}
      <section id="aup">
        <h2>3. Acceptable Use Policy</h2>
        <p>
          You agree to use the Service only for lawful purposes and in accordance with these Terms.
          The full Acceptable Use Policy is also available as a standalone document at{' '}
          <a href="/legal/aup">/legal/aup</a>. The following prohibited uses are non-exhaustive:
        </p>

        <h3>3.1 Prohibited Content</h3>
        <ul>
          <li>
            <strong>Child sexual abuse material (CSAM):</strong> Uploading, processing, generating,
            or distributing child sexual abuse material or any content that sexually exploits or
            depicts minors is absolutely prohibited. Any such activity will be reported immediately
            to the National Center for Missing and Exploited Children (NCMEC) and relevant law
            enforcement authorities. Accounts will be terminated immediately without notice or
            refund.
          </li>
          <li>
            <strong>Malware and malicious code:</strong> Uploading, processing, or distributing
            files containing viruses, trojans, ransomware, spyware, adware, or any other malicious
            software.
          </li>
          <li>
            <strong>Illegal content:</strong> Content that violates any applicable local, national,
            or international law, including content that infringes intellectual property rights,
            constitutes defamation or harassment, or facilitates illegal activities.
          </li>
          <li>
            <strong>Hate speech and harassment:</strong> Content that promotes violence, hatred, or
            discrimination based on race, ethnicity, religion, gender, sexual orientation,
            disability, or other protected characteristics.
          </li>
        </ul>

        <h3>3.2 Prohibited Conduct</h3>
        <ul>
          <li>
            <strong>Automated bulk uploads and scraping:</strong> Using automated scripts, bots, or
            tools to upload files or use the Service at a rate that exceeds normal human usage
            patterns without our prior written consent. This includes scraping Service content or
            using the web interface as an unofficial API.
          </li>
          <li>
            <strong>Reverse engineering:</strong> Decompiling, disassembling, reverse engineering,
            or attempting to derive the source code of any part of the Service, except to the extent
            expressly permitted by applicable law.
          </li>
          <li>
            <strong>Resale without authorisation:</strong> Reselling, sublicensing, or providing
            the Service to third parties as a white-label or embedded service without a separate
            commercial API licence agreement with {BRAND.companyEntity}.
          </li>
          <li>
            <strong>Impersonation:</strong> Impersonating {BRAND.name}, our employees, or any
            other person or entity, including creating fake support communications or phishing pages.
          </li>
          <li>
            <strong>Security interference:</strong> Attempting to probe, scan, or test
            vulnerabilities in the Service or circumvent authentication, rate limiting, access
            controls, or encryption. (Responsible security research is welcome — see
            {' '}<a href={`https://${BRAND.domain}/security.txt`}>/security.txt</a> for our
            safe-harbour policy.)
          </li>
          <li>
            <strong>Service disruption:</strong> Taking any action that imposes an unreasonable
            or disproportionately large load on our infrastructure, or interferes with the Service
            for other users (e.g., deliberate DDoS, resource exhaustion attacks).
          </li>
          <li>
            <strong>Data harvesting:</strong> Extracting or harvesting personal data of other users
            from the Service by any means.
          </li>
        </ul>

        <h3>3.3 Fair-Use Thresholds</h3>
        <p>
          Free tier accounts may process up to [TODO: set limit, e.g., 10 files per day or 50 MB
          per day] per day. Automated or programmatic use beyond these limits requires a paid API
          plan. We reserve the right to throttle or suspend accounts that exceed fair-use thresholds
          after reasonable notice.
        </p>
      </section>

      {/* 4. IP */}
      <section id="ip">
        <h2>4. Intellectual Property</h2>
        <p>
          <strong>Your files are your intellectual property.</strong> By uploading files to the
          Service, you grant {BRAND.companyEntity} a limited, non-exclusive, royalty-free,
          worldwide licence to process your files solely for the purpose of delivering the specific
          operation you request. This licence terminates automatically when your files are deleted
          (within 60 minutes of processing). We do not acquire any ownership right in your files.
        </p>
        <p>
          <strong>Our platform:</strong> The {BRAND.name} platform — including its software,
          trademarks, trade names, logos, visual design, and all non-user content — is owned by or
          licensed to {BRAND.companyEntity} and is protected by applicable intellectual property
          laws worldwide. You may not copy, modify, distribute, sell, or lease any part of the
          platform without our prior written consent, except as permitted by applicable law.
        </p>
        <p>
          <strong>Feedback:</strong> If you submit feedback, suggestions, or ideas about the
          Service, you grant us the right to use that feedback without restriction or compensation.
        </p>
      </section>

      {/* 5. Billing and Refunds */}
      <section id="billing">
        <h2>5. Pricing, Billing, and Refunds</h2>

        <h3>5.1 Pricing</h3>
        <p>
          Paid plans are billed in advance on a monthly or annual basis. All fees are stated in
          US Dollars (USD) unless otherwise noted. We reserve the right to change pricing with
          at least <strong>30 days&apos; notice</strong> via in-app notification and email to
          current subscribers. Continued use of a paid plan after the notice period constitutes
          acceptance of the new pricing.
        </p>

        <h3>5.2 Billing</h3>
        <p>
          By providing payment information, you authorise us (via Stripe, our payment processor)
          to charge you on the billing cycle you select (monthly or annual) until you cancel. You
          are responsible for ensuring your payment information is current. Failed payments will
          result in a grace period of [TODO: 7 days] before account suspension.
        </p>
        <p>
          Taxes (VAT, GST, sales tax) may apply depending on your location. Stripe automatically
          calculates applicable tax; the applicable tax rate will be shown at checkout.
        </p>

        <h3>5.3 Cancellation</h3>
        <p>
          You may cancel your subscription at any time from your account Settings &rarr; Billing.
          Cancellation takes effect at the end of the current billing period; you retain full access
          until then. We do not provide pro-rated refunds for unused time within a billing period,
          except as stated in the refund policy below.
        </p>

        <h3>5.4 Refund Policy</h3>
        <ul>
          <li>
            <strong>First-time subscribers:</strong> You may request a full refund within{' '}
            <strong>14 days</strong> of your initial subscription purchase if you are not satisfied.
            Email <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> with subject
            &quot;Refund Request&quot;. Refunds are processed to the original payment method within
            5–10 business days.
          </li>
          <li>
            <strong>Renewals:</strong> No refunds are issued for subscription renewals unless
            required by applicable law (see EU Consumer Rights below).
          </li>
          <li>
            <strong>Excessive use during refund window:</strong> If you have uploaded more than
            [TODO: e.g., 50] files or consumed more than [TODO: e.g., 500 MB] of processing
            capacity during the 14-day refund window, we reserve the right to deny the refund
            request.
          </li>
          <li>
            <strong>Annual plans:</strong> For annual plans, the 14-day refund window applies to
            the full annual fee if you cancel within 14 days of purchase.
          </li>
        </ul>
      </section>

      {/* 6. SLA */}
      <section id="sla">
        <h2>6. Service Levels and Availability</h2>
        <p>
          THE SERVICE IS PROVIDED &quot;AS IS&quot; — see Section 9 (Disclaimers). That said:
        </p>
        <ul>
          <li>
            <strong>Free tier:</strong> No uptime guarantee. The Service may be unavailable for
            maintenance, updates, or infrastructure issues. We will endeavour to provide advance
            notice of planned maintenance via status.{BRAND.domain}.
          </li>
          <li>
            <strong>Paid tiers (Pro and above):</strong> We target a{' '}
            <strong>99.5% monthly uptime</strong> for the core PDF processing API. If monthly
            uptime falls below 99.5%, eligible customers may receive service credits per
            Schedule A. [TODO: Schedule A — define credit calculation: e.g., 10% monthly fee
            credit per percentage point below 99.5%, up to 30% of monthly fee. Business decision
            required.]
          </li>
          <li>
            <strong>Exclusions:</strong> Downtime caused by scheduled maintenance (announced 48h
            in advance), third-party infrastructure failures (AWS, Cloudflare), force majeure,
            your actions or misuse, or events outside our reasonable control does not count toward
            monthly uptime calculations.
          </li>
        </ul>
        <p>
          Service status is published at{' '}
          <a
            href={`https://status.${BRAND.domain}`}
            target="_blank"
            rel="noopener noreferrer"
          >
            status.{BRAND.domain}
          </a>
          . Subscribe for incident notifications.
        </p>
      </section>

      {/* 7. DMCA */}
      <section id="dmca">
        <h2>7. Copyright / DMCA and EU Notice-and-Action</h2>

        <h3>7.1 US DMCA Takedown</h3>
        <p>
          We comply with the Digital Millennium Copyright Act (DMCA). If you believe that content
          accessible through the Service infringes your copyright, send a DMCA takedown notice to
          our designated agent:
        </p>
        <address className="not-italic">
          <strong>DMCA Designated Agent:</strong>
          <br />
          {BRAND.companyEntity}
          <br />
          {BRAND.legalAddress}
          <br />
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>
        </address>
        <p>Your notice must include:</p>
        <ol>
          <li>Your physical or electronic signature.</li>
          <li>Identification of the copyrighted work claimed to be infringed.</li>
          <li>Identification of the infringing material and its URL or location.</li>
          <li>Your contact information (name, address, phone, email).</li>
          <li>A statement that you have a good-faith belief that the use is not authorised.</li>
          <li>
            A statement that the information in the notice is accurate, and under penalty of
            perjury, that you are authorised to act on behalf of the copyright owner.
          </li>
        </ol>

        <h3>7.2 DMCA Counter-Notice</h3>
        <p>
          If you believe your content was removed in error, you may submit a counter-notice to the
          same address. Your counter-notice must include: your signature, identification of the
          removed material and its former location, a statement under penalty of perjury that the
          material was removed by mistake or misidentification, your contact information, and
          consent to the jurisdiction of your federal district court. We will forward the
          counter-notice to the original complainant; if no court action is filed within 10–14
          business days, we may restore the material.
        </p>

        <h3>7.3 EU Notice-and-Action</h3>
        <p>
          For users and rights-holders in the EU, we comply with the notice-and-action obligations
          under the EU E-Commerce Directive (2000/31/EC) and, where applicable, the Digital Services
          Act (DSA). To submit a notice of illegal content, email{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> with subject
          &quot;Illegal Content Notice&quot; and describe the content, its location, and the
          applicable law you believe it violates. We will act expeditiously upon receiving a valid
          notice.
        </p>

        <h3>7.4 Repeat Infringers</h3>
        <p>
          In appropriate circumstances and at our discretion, we will terminate accounts of repeat
          copyright infringers.
        </p>
      </section>

      {/* 8. Suspension and Termination */}
      <section id="suspension">
        <h2>8. Account Suspension and Termination</h2>

        <h3>8.1 Grounds for Suspension or Termination by Us</h3>
        <p>
          We may suspend or terminate your account — immediately and without prior notice in serious
          cases, or with reasonable advance notice for lesser violations — if:
        </p>
        <ul>
          <li>You breach any provision of these Terms, including the Acceptable Use Policy.</li>
          <li>We are required to do so by law, court order, or regulatory direction.</li>
          <li>
            We reasonably believe your account is being used fraudulently, for identity theft, or
            to cause harm to others.
          </li>
          <li>Your account remains inactive for more than 24 months (with advance notice).</li>
          <li>
            Your payment fails and is not resolved within the grace period specified in Section 5.2.
          </li>
        </ul>
        <p>
          Where we suspend your account for reasons other than the most serious violations (e.g.,
          CSAM, malware, fraud), we will endeavour to give you notice and a reasonable opportunity
          to cure the breach before termination, unless doing so would cause harm or is prohibited
          by law.
        </p>

        <h3>8.2 Data Export Window</h3>
        <p>
          Upon termination (by either party), your account enters a{' '}
          <strong>30-day export window</strong> during which you may download any available account
          data and export data using the data export function in Settings. After 30 days, your
          account data is permanently deleted in accordance with our{' '}
          <a href="/legal/privacy">Privacy Policy</a>. Note: uploaded files are deleted within 60
          minutes of upload regardless; only account metadata (history, settings) is subject to
          the 30-day window.
        </p>
        <p>
          If your account was terminated for a serious violation (e.g., CSAM, malware), the data
          export window does not apply and data will be deleted (or preserved for law enforcement)
          as legally required.
        </p>

        <h3>8.3 Termination by You</h3>
        <p>
          You may terminate your account at any time by going to Settings &rarr; Account &rarr;
          Delete Account, or by contacting{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>. Termination is
          effective when we confirm receipt. Termination does not entitle you to a refund except as
          provided in Section 5.4.
        </p>

        <h3>8.4 Effect of Termination</h3>
        <p>
          Upon termination, your right to access and use the Service ceases. All licences granted
          to you under these Terms terminate. Provisions that by their nature survive termination
          (including Sections 4, 9, 10, 11, 13, 15) will remain in effect.
        </p>
      </section>

      {/* 9. Disclaimers */}
      <section id="disclaimers">
        <h2>9. Disclaimers and Warranties</h2>
        <p>
          THE SERVICE IS PROVIDED &quot;AS IS&quot; AND &quot;AS AVAILABLE&quot; WITHOUT WARRANTIES
          OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO WARRANTIES OF
          MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT. WE DO NOT
          WARRANT THAT THE SERVICE WILL BE UNINTERRUPTED, ERROR-FREE, SECURE, OR FREE OF VIRUSES
          OR OTHER HARMFUL COMPONENTS.
        </p>
        <p>
          WE DO NOT WARRANT THAT: (A) THE SERVICE WILL MEET YOUR REQUIREMENTS; (B) RESULTS
          OBTAINED FROM USE OF THE SERVICE WILL BE ACCURATE OR RELIABLE; (C) ANY ERRORS IN THE
          SERVICE WILL BE CORRECTED; OR (D) AI-GENERATED OUTPUTS WILL BE ACCURATE, COMPLETE, OR
          SUITABLE FOR ANY PARTICULAR PURPOSE. AI FEATURES MAY PRODUCE INCORRECT OR MISLEADING
          OUTPUTS — ALWAYS VERIFY AI-GENERATED CONTENT BEFORE RELYING ON IT.
        </p>
        <p>
          <strong>Note for EU/UK consumers:</strong> Applicable consumer protection laws may grant
          you statutory rights that cannot be excluded. Nothing in these disclaimers is intended to
          limit those rights. See Section 12.
        </p>
      </section>

      {/* 10. Liability */}
      <section id="liability">
        <h2>10. Limitation of Liability</h2>
        <p>
          TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW,{' '}
          {BRAND.companyEntity.toUpperCase()} AND ITS OFFICERS, DIRECTORS, EMPLOYEES, AND AGENTS
          WILL NOT BE LIABLE FOR ANY INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR PUNITIVE
          DAMAGES, INCLUDING BUT NOT LIMITED TO LOSS OF PROFITS, LOSS OF DATA, BUSINESS
          INTERRUPTION, OR DAMAGE TO GOODWILL, ARISING FROM YOUR USE OF OR INABILITY TO USE THE
          SERVICE, EVEN IF WE HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
        </p>
        <p>
          OUR TOTAL AGGREGATE LIABILITY TO YOU FOR ALL CLAIMS ARISING FROM OR RELATING TO THE
          SERVICE OR THESE TERMS IS LIMITED TO THE GREATER OF: (A) THE TOTAL FEES YOU PAID TO US
          IN THE TWELVE (12) MONTHS IMMEDIATELY PRECEDING THE EVENT GIVING RISE TO THE CLAIM;
          OR (B) ONE HUNDRED US DOLLARS (USD $100).
        </p>
        <p>
          <strong>Carve-outs (liability cap does NOT apply to):</strong>
        </p>
        <ul>
          <li>Our gross negligence or wilful misconduct.</li>
          <li>Fraud or fraudulent misrepresentation.</li>
          <li>Death or personal injury caused by our negligence.</li>
          <li>Breach of our obligations under data protection law in respect of your personal data.</li>
          <li>Our IP infringement indemnity obligations under Section 11.</li>
          <li>
            Any liability that cannot be limited or excluded under applicable law (including EU and
            UK consumer protection law).
          </li>
        </ul>
      </section>

      {/* 11. Indemnification */}
      <section id="indemnification">
        <h2>11. Indemnification (Mutual)</h2>

        <h3>11.1 Your Indemnity</h3>
        <p>
          You agree to indemnify, defend, and hold harmless {BRAND.companyEntity} and its
          affiliates, officers, directors, employees, and agents from and against any claims,
          liabilities, damages, losses, and expenses (including reasonable legal fees) arising out
          of or in connection with: (a) your use of the Service in violation of these Terms; (b)
          your uploaded content infringing or allegedly infringing any third-party intellectual
          property rights; (c) your violation of applicable law; or (d) your negligence or wilful
          misconduct.
        </p>

        <h3>11.2 Our IP Indemnity</h3>
        <p>
          We will indemnify, defend, and hold harmless you from and against third-party claims
          alleging that the Service (excluding your uploaded content and third-party components)
          infringes a valid patent, copyright, trademark, or trade secret. This indemnity does not
          apply where: (a) you have modified the Service in an unauthorised manner; (b) the claim
          arises from your combination of the Service with a third-party product we did not approve;
          or (c) you continued using a version of the Service after being notified of a
          non-infringing alternative.
        </p>
      </section>

      {/* 12. EU Consumer */}
      <section id="eu-consumer">
        <h2>12. EU / EEA and UK Consumer Rights</h2>
        <p>
          If you are a consumer (an individual acting for purposes outside your trade, business, or
          profession) in the EU, EEA, or UK, the following applies:
        </p>
        <ul>
          <li>
            <strong>Mandatory consumer protection laws prevail:</strong> Nothing in these Terms
            limits or excludes any rights you have under applicable mandatory consumer protection
            law. Where any provision conflicts with such law, the law prevails.
          </li>
          <li>
            <strong>Right of withdrawal (EU Consumer Rights Directive, Art. 9):</strong> For
            digital subscriptions, you have a <strong>14-day right of withdrawal</strong> (cooling-off
            period) from the date of purchase, unless you expressly consent to immediate provision of
            the digital service and acknowledge that the right of withdrawal is thereby lost.
            [TODO: Implement a checkout flow with an explicit withdrawal waiver checkbox stating:
            &quot;I agree that the service will be provided immediately and I understand that I
            thereby waive my right of withdrawal.&quot; — required before EU launch.]
          </li>
          <li>
            <strong>EU Online Dispute Resolution:</strong> You may refer disputes to the EU Online
            Dispute Resolution platform at{' '}
            <a
              href="https://ec.europa.eu/odr"
              target="_blank"
              rel="noopener noreferrer"
            >
              ec.europa.eu/odr
            </a>
            .
          </li>
          <li>
            <strong>Arbitration not enforceable for EU B2C:</strong> The arbitration clause in
            Section 13 is not enforceable against EU/EEA consumers. EU consumers may bring claims
            before the courts of their country of habitual residence.
          </li>
          <li>
            <strong>UK consumers:</strong> UK consumer law (Consumer Rights Act 2015, Consumer
            Contracts Regulations 2013) applies to UK residents, and the class action waiver in
            Section 13 does not affect your ability to bring individual claims before UK courts.
          </li>
        </ul>
      </section>

      {/* 13. Arbitration */}
      <section id="arbitration">
        <h2>13. Dispute Resolution and Arbitration</h2>
        <p>
          <strong>Informal resolution first:</strong> Before initiating any formal proceeding,
          please email <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> with a
          description of your dispute. We will attempt to resolve it informally within 30 days.
        </p>
        <p>
          <strong>Binding arbitration (non-EU/UK users):</strong> If informal resolution fails,
          any dispute, claim, or controversy arising from or relating to these Terms or the Service
          will be resolved by binding individual arbitration administered by JAMS (
          <a href="https://www.jamsadr.com" target="_blank" rel="noopener noreferrer">
            jamsadr.com
          </a>
          ) under its Streamlined Arbitration Rules, or by the American Arbitration Association
          (AAA) under its Consumer Arbitration Rules, at our election. The arbitration will be
          conducted in [TODO: specify — e.g., Wilmington, Delaware, or by video conference].
          The arbitrator&apos;s decision will be final and binding, subject to limited review under
          applicable arbitration law.
        </p>
        <p>
          Either party may seek injunctive or other equitable relief from a court of competent
          jurisdiction to prevent irreparable harm, without waiving arbitration rights.
        </p>
        <p>
          <strong>Class action waiver:</strong> ALL CLAIMS MUST BE BROUGHT ON AN INDIVIDUAL BASIS.
          YOU AND {BRAND.companyEntity.toUpperCase()} AGREE TO WAIVE ANY RIGHT TO PARTICIPATE IN
          A CLASS ACTION, COLLECTIVE ARBITRATION, OR REPRESENTATIVE PROCEEDING. The arbitrator
          may not consolidate claims of multiple persons.
        </p>
        <p>
          <strong>Opt-out:</strong> You may opt out of the arbitration agreement and class action
          waiver by emailing{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> with subject
          &quot;Arbitration Opt-Out&quot; within <strong>30 days</strong> of first accepting these
          Terms. If you opt out, disputes will be resolved in the courts specified in Section 15.
        </p>
        <p>
          <strong>EU/UK consumer carve-out:</strong> The arbitration clause and class action waiver
          in this Section do not apply to consumers in the EU, EEA, or UK. See Section 12.
        </p>
      </section>

      {/* 14. Force Majeure */}
      <section id="force-majeure">
        <h2>14. Force Majeure</h2>
        <p>
          Neither party will be liable to the other for any delay or failure to perform its
          obligations under these Terms to the extent that such delay or failure is caused by
          circumstances beyond that party&apos;s reasonable control, including but not limited to
          acts of God, natural disasters, pandemic or epidemic, war, terrorism, riots, government
          action, power or internet outages, strikes or labour disputes, or failures of third-party
          infrastructure providers (e.g., AWS, Cloudflare). The affected party must notify the
          other as soon as practicable and make commercially reasonable efforts to mitigate the
          impact.
        </p>
      </section>

      {/* 15. General */}
      <section id="general">
        <h2>15. General Provisions</h2>

        <h3>15.1 Governing Law</h3>
        <p>
          These Terms are governed by and construed in accordance with the laws of the State of
          Delaware, United States, without regard to conflict-of-law provisions. [TODO: Confirm
          with counsel based on company HQ jurisdiction. If a separate EU entity (GmbH/Ltd) is
          established, EU customer contracts may be governed by the law of that entity&apos;s
          jurisdiction — e.g., Ireland or Germany. Dual-entity model common for international SaaS.]
        </p>
        <p>
          For non-arbitration disputes (or EU/UK consumer disputes), the exclusive jurisdiction of
          the courts of [TODO: Delaware / specified jurisdiction] applies, except for EU/UK
          consumers who may sue in their country of habitual residence.
        </p>

        <h3>15.2 Notices</h3>
        <p>
          Notices from you to us must be sent by email to{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a> or by post to our
          registered address (see below). Notices from us to you will be delivered via in-app
          notification and/or to the email address registered with your account. Notices are
          effective: (a) email — on the next business day after sending; (b) post — 3 business
          days after posting.
        </p>

        <h3>15.3 Assignment</h3>
        <p>
          We may assign, transfer, or novate our rights and obligations under these Terms to an
          affiliate or in connection with a merger, acquisition, sale of assets, or corporate
          reorganisation without your consent, provided the assignee agrees to be bound by these
          Terms and the Privacy Policy. We will notify you of any such assignment. You may not
          assign or transfer your rights or obligations without our prior written consent. Any
          purported assignment by you without such consent is void.
        </p>

        <h3>15.4 Severability</h3>
        <p>
          If any provision of these Terms is held to be invalid, illegal, or unenforceable by a
          court of competent jurisdiction, that provision will be modified to the minimum extent
          necessary to make it enforceable, or severed if modification is not possible. The
          remaining provisions will continue in full force and effect.
        </p>

        <h3>15.5 Entire Agreement</h3>
        <p>
          These Terms, together with the Privacy Policy, Cookie Policy, and any order forms or
          addenda (including any signed DPA), constitute the entire agreement between you and{' '}
          {BRAND.companyEntity} with respect to the Service and supersede all prior agreements,
          representations, and understandings. No waiver of any provision will constitute a waiver
          of any other provision or of the same provision on a different occasion.
        </p>

        <h3>15.6 No Waiver</h3>
        <p>
          Our failure to exercise or enforce any right or provision of these Terms will not
          constitute a waiver of that right or provision.
        </p>

        <h3>15.7 Relationship of Parties</h3>
        <p>
          The parties are independent contractors. Nothing in these Terms creates a partnership,
          joint venture, agency, or employment relationship between you and {BRAND.companyEntity}.
        </p>
      </section>

      {/* Contact */}
      <section id="contact">
        <h2>Contact</h2>
        <address className="not-italic">
          <strong>{BRAND.companyEntity}</strong>
          <br />
          {BRAND.legalAddress}
          <br />
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>
        </address>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
