import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Data Processing Agreement (DPA)',
  description: `Data Processing Agreement for ${BRAND.name} — full GDPR Art. 28 structure including SCCs, UK IDTA, audit rights, breach notification, and 14 operational sections.`,
};

const LAST_UPDATED = '2026-05-16';

export default function DpaPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Data Processing Agreement (DPA)</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      {/* Request signable copy CTA */}
      <div className="not-prose mb-8 rounded-lg border border-neutral-200 bg-neutral-50 p-4 dark:border-neutral-700 dark:bg-neutral-900">
        <p className="text-sm text-neutral-700 dark:text-neutral-300">
          <strong>Enterprise customers:</strong> To receive a countersigned PDF copy of this DPA
          for your compliance records, click the button below.
        </p>
        <a
          href={`mailto:${BRAND.dpoEmail}?subject=DPA%20Request%20%E2%80%94%20%5BYour%20organisation%20name%5D&body=Please%20send%20us%20a%20countersigned%20DPA.%0A%0AOrganisation%20name%3A%20%0A%0ACountry%20of%20incorporation%3A%20%0A%0A${BRAND.name}%20account%20email%3A%20`}
          className="mt-3 inline-block rounded bg-blue-600 px-4 py-2 text-sm font-semibold text-white no-underline hover:bg-blue-700"
        >
          Request a Signable Copy
        </a>
      </div>

      <nav aria-label="Table of contents">
        <ol className="text-sm">
          <li><a href="#preamble">Preamble</a></li>
          <li><a href="#s1">1. Definitions</a></li>
          <li><a href="#s2">2. Subject Matter and Duration</a></li>
          <li><a href="#s3">3. Nature and Purpose of Processing</a></li>
          <li><a href="#s4">4. Categories of Personal Data and Data Subjects</a></li>
          <li><a href="#s5">5. Obligations of the Processor</a></li>
          <li><a href="#s6">6. Subprocessors</a></li>
          <li><a href="#s7">7. International Transfers</a></li>
          <li><a href="#s8">8. Audit Rights</a></li>
          <li><a href="#s9">9. Data Breach Notification</a></li>
          <li><a href="#s10">10. Data Subject Requests</a></li>
          <li><a href="#s11">11. Return or Deletion at End of Services</a></li>
          <li><a href="#s12">12. Liability</a></li>
          <li><a href="#s13">13. Term and Termination</a></li>
          <li><a href="#s14">14. Governing Law and Jurisdiction</a></li>
          <li><a href="#schedule1">Schedule 1: Details of Processing</a></li>
          <li><a href="#schedule2">Schedule 2: Technical and Organisational Security Measures</a></li>
          <li><a href="#schedule3">Schedule 3: Approved Subprocessors</a></li>
        </ol>
      </nav>

      <section id="preamble">
        <h2>Preamble</h2>
        <p>
          This Data Processing Agreement (&quot;DPA&quot;) is entered into between:
        </p>
        <ul>
          <li>
            <strong>Controller</strong> (Customer): The entity or individual identified in the
            {BRAND.name} account (&quot;Customer&quot; or &quot;Controller&quot;); and
          </li>
          <li>
            <strong>Processor</strong>: {BRAND.companyEntity}, {BRAND.legalAddress}{' '}
            (&quot;{BRAND.name}&quot; or &quot;Processor&quot;).
          </li>
        </ul>
        <p>
          This DPA is incorporated into and forms part of the {BRAND.name}{' '}
          <a href="/legal/terms">Terms of Service</a> (&quot;Main Agreement&quot;). To the extent
          of any conflict between this DPA and the Main Agreement regarding the processing of
          personal data, this DPA shall prevail.
        </p>
        <p>
          This DPA applies to the extent that {BRAND.name} processes personal data on behalf of
          the Customer as a data processor within the meaning of GDPR Article 4(8).
        </p>
      </section>

      <section id="s1">
        <h2>1. Definitions</h2>
        <p>In this DPA, the following terms have the meanings given by GDPR Article 4:</p>
        <ul>
          <li>
            <strong>&quot;Personal Data&quot;</strong> means any information relating to an
            identified or identifiable natural person (Article 4(1)).
          </li>
          <li>
            <strong>&quot;Controller&quot;</strong> means the natural or legal person that
            determines the purposes and means of processing personal data (Article 4(7)).
          </li>
          <li>
            <strong>&quot;Processor&quot;</strong> means a natural or legal person that processes
            personal data on behalf of the controller (Article 4(8)).
          </li>
          <li>
            <strong>&quot;Sub-processor&quot;</strong> means any processor engaged by{' '}
            {BRAND.name} who processes personal data in connection with the Service on the
            Controller&apos;s behalf.
          </li>
          <li>
            <strong>&quot;Processing&quot;</strong> has the meaning in Article 4(2).
          </li>
          <li>
            <strong>&quot;Data Subject&quot;</strong> means an identified or identifiable natural
            person whose personal data is processed (Article 4(1)).
          </li>
          <li>
            <strong>&quot;Personal Data Breach&quot;</strong> means a breach of security leading
            to the accidental or unlawful destruction, loss, alteration, unauthorised disclosure
            of, or access to, personal data (Article 4(12)).
          </li>
          <li>
            <strong>&quot;GDPR&quot;</strong> means Regulation (EU) 2016/679.
          </li>
          <li>
            <strong>&quot;SCCs&quot;</strong> means the Standard Contractual Clauses for the
            transfer of personal data to third countries, approved by the European Commission
            (Decision 2021/914/EU).
          </li>
          <li>
            <strong>&quot;UK GDPR&quot;</strong> means the GDPR as retained in UK law by the
            European Union (Withdrawal) Act 2018, as amended by the Data Protection, Privacy and
            Electronic Communications (Amendments etc.) (EU Exit) Regulations 2019.
          </li>
          <li>
            <strong>&quot;UK IDTA&quot;</strong> means the International Data Transfer Agreement
            issued by the UK Information Commissioner&apos;s Office, effective 21 March 2022.
          </li>
        </ul>
      </section>

      <section id="s2">
        <h2>2. Subject Matter and Duration of Processing</h2>
        <p>
          <strong>Subject matter:</strong> {BRAND.name} processes personal data on behalf of the
          Customer solely to provide the {BRAND.name} PDF productivity service as described in the
          Main Agreement and as further specified in Schedule 1.
        </p>
        <p>
          <strong>Duration:</strong> {BRAND.name} will process personal data for the duration of
          the Main Agreement and, thereafter, only as required for data retention obligations under
          applicable law or as instructed by the Customer during the post-termination transition
          period (Section 11).
        </p>
      </section>

      <section id="s3">
        <h2>3. Nature and Purpose of Processing</h2>
        <p>
          {BRAND.name} provides a privacy-first PDF productivity platform enabling the Customer
          and the Customer&apos;s end users to upload, process, transform, sign, and annotate PDF
          documents. Processing activities include:
        </p>
        <ul>
          <li>Accepting and temporarily storing uploaded documents (max 60 minutes).</li>
          <li>
            Executing requested PDF operations (merge, split, compress, convert, OCR, e-signature,
            AI-assisted extraction/summarisation).
          </li>
          <li>Delivering processed outputs to authenticated users.</li>
          <li>
            Maintaining account data, billing records, and audit logs necessary to operate and
            support the Service.
          </li>
          <li>
            Transmitting document text to AI sub-processors (Anthropic, OpenAI) solely when AI
            features are explicitly invoked by the user.
          </li>
        </ul>
        <p>
          {BRAND.name} processes personal data only on the documented instructions of the Customer,
          including as set out in the Main Agreement and this DPA, unless required to do so by
          applicable law.
        </p>
      </section>

      <section id="s4">
        <h2>4. Categories of Personal Data and Data Subjects</h2>
        <p>See Schedule 1 for the full details of processing. In summary:</p>
        <ul>
          <li>
            <strong>Data subjects:</strong> The Customer&apos;s employees, contractors, or end
            users who create accounts or upload documents to the Service.
          </li>
          <li>
            <strong>Categories of personal data:</strong> Account identifiers (email, name),
            authentication tokens, uploaded document content (which may incidentally contain
            personal data about third parties), billing information (name, address, last-4 card
            digits), usage logs (IP address, user-agent, feature events), and AI-generated
            embeddings (deleted on document expiry).
          </li>
          <li>
            <strong>Special categories:</strong> We do not intentionally process special category
            data (GDPR Art. 9). If the Customer uploads documents containing health, racial,
            religious, or other special category data, the Customer remains the controller of that
            data and is responsible for ensuring a valid Art. 9 legal basis.
          </li>
        </ul>
      </section>

      <section id="s5">
        <h2>5. Obligations of the Processor</h2>
        <p>
          Pursuant to GDPR Article 28(3), {BRAND.name} agrees to:
        </p>
        <ol>
          <li>
            <strong>Process only on instruction (Art. 28(3)(a)):</strong> Process personal data
            only on documented instructions from the Controller, including with regard to transfers
            to third countries, unless required to do so by applicable law. If required to process
            without instruction, {BRAND.name} will inform the Controller as soon as legally
            permissible.
          </li>
          <li>
            <strong>Confidentiality (Art. 28(3)(b)):</strong> Ensure that persons authorised to
            process personal data are bound by appropriate confidentiality obligations (contractual
            or statutory).
          </li>
          <li>
            <strong>Security measures (Art. 28(3)(c)):</strong> Implement and maintain appropriate
            technical and organisational security measures (TOMs) per GDPR Article 32. See
            Schedule 2 and our{' '}
            <a href="/legal/security">Security page</a> for details.
          </li>
          <li>
            <strong>Sub-processor management (Art. 28(3)(d)):</strong> Engage sub-processors only
            with the Controller&apos;s general written authorisation (given by acceptance of this
            DPA). Notify the Controller before adding or replacing sub-processors (30 days&apos;
            advance notice per Section 6). Impose equivalent data protection obligations on all
            sub-processors and remain liable for their compliance.
          </li>
          <li>
            <strong>Data subject rights assistance (Art. 28(3)(e)):</strong> Assist the Controller
            by implementing appropriate technical and organisational measures, to fulfil the
            Controller&apos;s obligation to respond to data subject rights requests. See Section 10.
          </li>
          <li>
            <strong>Controller obligations assistance (Art. 28(3)(f)):</strong> Assist the
            Controller in ensuring compliance with GDPR Articles 32–36 (security, breach
            notification, DPIAs, prior consultation) given the nature of processing and information
            available to {BRAND.name}.
          </li>
          <li>
            <strong>Return or deletion (Art. 28(3)(g)):</strong> At the Controller&apos;s choice,
            delete or return all personal data to the Controller at the end of the provision of
            services, and delete all existing copies unless applicable law requires storage. See
            Section 11.
          </li>
          <li>
            <strong>Audit cooperation (Art. 28(3)(h)):</strong> Make available all information
            necessary to demonstrate compliance with GDPR Article 28, and allow and contribute to
            audits and inspections conducted by the Controller or its mandated auditor. See Section 8.
          </li>
        </ol>
      </section>

      <section id="s6">
        <h2>6. Sub-processors</h2>
        <p>
          By accepting this DPA (or the Main Agreement), the Controller grants {BRAND.name} general
          written authorisation to engage the sub-processors listed in Schedule 3 and at{' '}
          <a href="/legal/subprocessors">/legal/subprocessors</a>.
        </p>
        <p>
          <strong>New or changed sub-processors:</strong> {BRAND.name} will notify the Controller
          at least <strong>30 days</strong> before adding a new sub-processor or making material
          changes to an existing sub-processor that affect the nature of personal data processing.
          Notification will be via email to the Controller&apos;s registered account email address
          and via an in-app notice.
        </p>
        <p>
          <strong>Objection right:</strong> The Controller may object to a new sub-processor within
          14 days of receiving notice by notifying{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>. If the Controller objects and
          {BRAND.name} cannot accommodate the objection, the Controller may terminate the
          subscription before the change takes effect and receive a pro-rated refund of prepaid fees.
          Failure to object within 14 days constitutes acceptance.
        </p>
        <p>
          {BRAND.name} will impose data protection obligations on all sub-processors equivalent to
          those in this DPA. {BRAND.name} remains liable to the Controller for the performance of
          sub-processors&apos; obligations to the extent {BRAND.name} is liable under GDPR.
        </p>
      </section>

      <section id="s7">
        <h2>7. International Transfers</h2>
        <p>
          Where {BRAND.name} transfers personal data from the EU/EEA or UK to processors in a
          country without an adequacy decision, the transfer is governed by:
        </p>
        <ul>
          <li>
            <strong>EU SCCs (Decision 2021/914/EU):</strong> The Processor-to-Sub-processor module
            (Module 3) of the 2021 SCCs is incorporated by reference into each sub-processor
            agreement for transfers from the EU/EEA to the United States and other non-adequate
            countries. The SCCs are available at{' '}
            <a
              href="https://eur-lex.europa.eu/eli/dec_impl/2021/914/oj"
              target="_blank"
              rel="noopener noreferrer"
            >
              eur-lex.europa.eu (Decision 2021/914/EU)
            </a>
            .
          </li>
          <li>
            <strong>UK IDTA Addendum:</strong> For transfers from the UK, the UK International
            Data Transfer Agreement Addendum (Version B1.0, effective 21 March 2022) is appended
            to applicable sub-processor agreements, extending the EU SCC protections to UK data
            subjects.
          </li>
          <li>
            <strong>EU-US Data Privacy Framework:</strong> Where a sub-processor is DPF-certified,
            the DPF certification serves as a supplementary or alternative adequacy mechanism.
          </li>
        </ul>
        <p>
          Where the Controller-to-Processor module (Module 2) SCCs apply between the Controller
          and {BRAND.name} (i.e., where the Controller is in the EU/EEA and {BRAND.name} is not),
          those SCCs are incorporated into this DPA by reference and take precedence over any
          conflicting term in this DPA. Requests for SCC copies: email{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
        </p>
      </section>

      <section id="s8">
        <h2>8. Audit Rights</h2>
        <p>
          The Controller has the right to audit {BRAND.name}&apos;s compliance with this DPA and
          GDPR Article 28, subject to the following conditions:
        </p>
        <ul>
          <li>
            <strong>Frequency:</strong> No more than once per 12-month period, except where a
            Personal Data Breach has occurred, in which case an additional audit may be requested
            within a reasonable period after the breach.
          </li>
          <li>
            <strong>Notice:</strong> The Controller must give at least <strong>30 days&apos;</strong>{' '}
            prior written notice specifying the scope and proposed dates for the audit.
          </li>
          <li>
            <strong>Auditor:</strong> The audit may be conducted by the Controller&apos;s internal
            audit team or a mutually agreed independent third-party auditor who is bound by
            confidentiality obligations. {BRAND.name} may refuse to grant access to an auditor
            that is a competitor of {BRAND.name}.
          </li>
          <li>
            <strong>Scope:</strong> The audit is limited to records, systems, and facilities
            relevant to {BRAND.name}&apos;s processing of the Controller&apos;s personal data.
            The Controller shall not access data belonging to other {BRAND.name} customers.
          </li>
          <li>
            <strong>Cost:</strong> The Controller bears the cost of the audit. If the audit
            reveals a material breach of this DPA attributable to {BRAND.name}, {BRAND.name}
            bears the reasonable costs of remediation.
          </li>
          <li>
            <strong>Alternative:</strong> {BRAND.name} may satisfy audit obligations by providing
            an up-to-date third-party audit report (e.g., SOC 2 Type II, ISO 27001) that covers
            the relevant controls. The Controller may waive the right to a direct audit upon
            receipt of a satisfactory report.
          </li>
        </ul>
      </section>

      <section id="s9">
        <h2>9. Data Breach Notification</h2>
        <p>
          {BRAND.name} will notify the Controller of a confirmed Personal Data Breach{' '}
          <strong>without undue delay</strong> and where feasible, within{' '}
          <strong>72 hours</strong> of becoming aware of the breach, to the extent required by
          GDPR Article 33(2).
        </p>
        <p>The notification will include, to the extent known at the time of notification:</p>
        <ul>
          <li>The nature of the Personal Data Breach (Art. 33(3)(a)).</li>
          <li>
            The categories and approximate number of data subjects concerned (Art. 33(3)(a)).
          </li>
          <li>
            The categories and approximate number of personal data records concerned (Art. 33(3)(a)).
          </li>
          <li>
            The name and contact details of the data protection officer or other contact point
            (Art. 33(3)(b)).
          </li>
          <li>
            The likely consequences of the Personal Data Breach (Art. 33(3)(c)).
          </li>
          <li>
            The measures taken or proposed by {BRAND.name} to address the breach, including
            measures to mitigate its possible adverse effects (Art. 33(3)(d)).
          </li>
        </ul>
        <p>
          Where all required information is not available within 72 hours, {BRAND.name} will
          provide a preliminary notification within 72 hours and supplement it with additional
          information as soon as practicable.
        </p>
        <p>
          The Controller is responsible for assessing whether to notify the relevant supervisory
          authority (GDPR Art. 33) and affected data subjects (GDPR Art. 34). {BRAND.name} will
          provide reasonable assistance in preparing such notifications.
        </p>
        <p>
          {BRAND.name}&apos;s security contact:{' '}
          <a href={`mailto:${BRAND.securityEmail}`}>{BRAND.securityEmail}</a>.
        </p>
      </section>

      <section id="s10">
        <h2>10. Data Subject Requests</h2>
        <p>
          {BRAND.name} will implement appropriate technical and organisational measures to assist
          the Controller in fulfilling its obligations to respond to data subject rights requests
          (GDPR Chapter III, including Articles 15–22).
        </p>
        <p>
          Where {BRAND.name} receives a data subject rights request directly from a data subject
          relating to the Controller&apos;s data, {BRAND.name} will promptly notify the Controller
          (where legally permissible) and, unless instructed otherwise by the Controller, refrain
          from responding directly to the data subject.
        </p>
        <p>
          {BRAND.name} will provide the Controller with reasonable assistance in responding to
          requests within <strong>10 business days</strong> of the Controller&apos;s instruction.
          Assistance beyond this scope may be subject to reasonable fees.
        </p>
        <p>
          The first data subject request assistance per 12-month period is provided at no charge.
          Subsequent requests may incur a reasonable administrative fee reflecting {BRAND.name}&apos;s
          actual costs.
        </p>
      </section>

      <section id="s11">
        <h2>11. Return or Deletion at End of Services</h2>
        <p>
          Upon termination or expiry of the Main Agreement, or upon the Controller&apos;s written
          request during the term:
        </p>
        <ul>
          <li>
            <strong>Return:</strong> {BRAND.name} will make available a data export (in a common
            machine-readable format, such as JSON or CSV) of all Controller personal data
            (excluding uploaded files, which are deleted within 60 minutes of upload) within{' '}
            <strong>30 days</strong> of request.
          </li>
          <li>
            <strong>Deletion:</strong> After the 30-day export window, {BRAND.name} will
            securely delete all remaining Controller personal data from its systems and
            sub-processors&apos; systems (except where retention is required by applicable law).
          </li>
          <li>
            <strong>Certificate of destruction:</strong> Available on request. Email{' '}
            <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a> with subject
            &quot;Certificate of Destruction Request&quot;.
          </li>
          <li>
            <strong>Exceptions:</strong> {BRAND.name} may retain billing records for 7 years as
            required by tax and accounting law, and may retain anonymised or aggregated data that
            cannot be linked to an individual.
          </li>
        </ul>
      </section>

      <section id="s12">
        <h2>12. Liability</h2>
        <p>
          Each party&apos;s liability under this DPA (including under the SCCs, where incorporated)
          is subject to the liability cap and carve-outs set out in Section 10 of the Main
          Agreement (Limitation of Liability), except:
        </p>
        <ul>
          <li>
            The liability cap does not apply to either party&apos;s liability under the SCCs
            (where applicable) to the extent that the SCCs impose mandatory liability that cannot
            be contractually limited.
          </li>
          <li>
            Neither party may exclude liability for data breaches caused by that party&apos;s gross
            negligence or wilful misconduct.
          </li>
        </ul>
      </section>

      <section id="s13">
        <h2>13. Term and Termination</h2>
        <p>
          This DPA is coterminous with the Main Agreement. It enters into force on the date the
          Customer first accepts the Main Agreement (or, for existing customers, on the date of
          mutual execution of a signed DPA) and remains in force until the Main Agreement expires
          or is terminated, at which point this DPA automatically terminates, subject to the
          survival of Sections 1, 5, 9, 11, and 12.
        </p>
      </section>

      <section id="s14">
        <h2>14. Governing Law and Jurisdiction</h2>
        <p>
          This DPA is governed by the same governing law and jurisdiction as the Main Agreement
          (Section 15.1 of the Terms of Service), unless the applicable SCCs specify otherwise,
          in which case the law and jurisdiction specified in the SCCs shall prevail for matters
          covered by the SCCs.
        </p>
        <p>
          [TODO: Confirm governing law with counsel — Delaware law for US entity; EU member state
          law if a separate EU entity is established. EU SCCs require an EU member state law for
          Module 2/3 where one party is in the EU.]
        </p>
      </section>

      {/* Schedules */}
      <section id="schedule1">
        <h2>Schedule 1: Details of Processing</h2>

        <h3>A. List of Parties</h3>
        <ul>
          <li>
            <strong>Controller (Data exporter):</strong> The entity identified in the{' '}
            {BRAND.name} account.
          </li>
          <li>
            <strong>Processor (Data importer):</strong> {BRAND.companyEntity},{' '}
            {BRAND.legalAddress}.
          </li>
        </ul>

        <h3>B. Description of Transfer / Processing</h3>
        <ul>
          <li>
            <strong>Categories of data subjects:</strong> The Controller&apos;s employees,
            contractors, and/or end users who access and use the {BRAND.name} Service.
          </li>
          <li>
            <strong>Categories of personal data:</strong> Account identifiers (name, email),
            authentication tokens, uploaded document content (including any personal data
            incidentally contained therein), payment identifiers (last-4 card digits, billing
            name/address), usage logs (IP address, user-agent, feature events), AI embeddings
            (transient, deleted on document expiry).
          </li>
          <li>
            <strong>Special categories of personal data (Art. 9):</strong> None intentionally
            processed. If present incidentally in uploaded documents, the Controller is
            responsible for the legal basis.
          </li>
          <li>
            <strong>Nature of processing:</strong> Storage, retrieval, transformation (merge,
            split, compress, convert, OCR, e-sign), AI analysis (when invoked), transmission to
            sub-processors, deletion.
          </li>
          <li>
            <strong>Purpose of transfer:</strong> Delivery of the {BRAND.name} PDF productivity
            service as contracted.
          </li>
          <li>
            <strong>Retention period:</strong> Uploaded files: max 60 minutes. Account data:
            duration of agreement + 30-day grace period. Billing records: 7 years. See{' '}
            <a href="/legal/privacy#retention">Privacy Policy, Section 5</a>.
          </li>
          <li>
            <strong>Frequency:</strong> Continuous (on-demand per user action).
          </li>
        </ul>

        <h3>C. Competent Supervisory Authority</h3>
        <p>
          [TODO: Specify the supervisory authority with jurisdiction over {BRAND.name} as
          processor. If the Controller&apos;s main establishment is in an EU member state, the
          supervisory authority of that state. If {BRAND.name} establishes an EU entity, the
          supervisory authority of that entity&apos;s member state. UK: Information
          Commissioner&apos;s Office (ico.org.uk).]
        </p>
      </section>

      <section id="schedule2">
        <h2>Schedule 2: Technical and Organisational Security Measures (TOMs)</h2>
        <p>
          The security measures implemented by {BRAND.name} are described in detail on our{' '}
          <a href="/legal/security">Security page</a>. In summary, they include:
        </p>
        <ul>
          <li>
            <strong>Encryption:</strong> TLS 1.3 in transit; AES-256 at rest via AWS KMS on S3,
            RDS, and EBS.
          </li>
          <li>
            <strong>Access control:</strong> RBAC with principle of least privilege; MFA mandatory
            for all production access; no shared accounts; just-in-time (JIT) access for
            privileged operations.
          </li>
          <li>
            <strong>Network segregation:</strong> VPC private subnets; NetworkPolicy default-deny;
            PDF processing workers have no internet egress.
          </li>
          <li>
            <strong>Sandboxing:</strong> PDF processing workers run in containers with
            --read-only filesystem, --cap-drop=ALL, and no outbound network access.
          </li>
          <li>
            <strong>Logging and monitoring:</strong> Centralised via OpenTelemetry → Grafana Cloud;
            immutable audit logs; automated alerting on anomalies.
          </li>
          <li>
            <strong>Vulnerability management:</strong> Dependabot daily dependency scans; CodeQL
            SAST on all PRs; annual third-party penetration tests.
          </li>
          <li>
            <strong>Backup and DR:</strong> RDS PITR (35 days); Multi-AZ failover; RPO ≤ 5 min;
            RTO ≤ 1 hour; quarterly restore drills.
          </li>
          <li>
            <strong>Personnel security:</strong> Background checks for employees with access to
            production; security training at onboarding and annually.
          </li>
        </ul>
      </section>

      <section id="schedule3">
        <h2>Schedule 3: Approved Sub-processors</h2>
        <p>
          The current list of approved sub-processors is maintained at{' '}
          <a href="/legal/subprocessors">pdfmaster.app/legal/subprocessors</a> and is incorporated
          into this DPA by reference. Additions or changes are notified per Section 6 of this DPA.
        </p>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
