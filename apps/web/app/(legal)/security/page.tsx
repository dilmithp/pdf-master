import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Security',
  description: `Security practices at ${BRAND.name}: encryption, access controls, network architecture, incident response, compliance roadmap, and responsible disclosure.`,
};

const LAST_UPDATED = '2026-05-16';

interface ComplianceRow {
  standard: string;
  status: string;
  eta: string;
  notes: string;
}

const COMPLIANCE: ComplianceRow[] = [
  {
    standard: 'GDPR (EU 2016/679)',
    status: 'Implemented',
    eta: 'Current',
    notes: 'Privacy Policy, DPA (GDPR Art. 28), SCCs, UK IDTA, DPO appointed',
  },
  {
    standard: 'UK GDPR',
    status: 'Implemented',
    eta: 'Current',
    notes: 'UK IDTA Addendum in subprocessor agreements; ICO registration [TODO]',
  },
  {
    standard: 'CCPA / CPRA (California)',
    status: 'Implemented',
    eta: 'Current',
    notes: 'No sale/sharing; GPC signal honoured; consumer rights process in place',
  },
  {
    standard: 'SOC 2 Type 1',
    status: 'In progress',
    eta: 'Q3 2026 (target)',
    notes: 'Readiness assessment complete; audit engagement underway',
  },
  {
    standard: 'SOC 2 Type 2',
    status: 'Planned',
    eta: 'Q1 2027 (target)',
    notes: 'Follows Type 1; 6-month observation period required',
  },
  {
    standard: 'ISO 27001',
    status: 'Under evaluation',
    eta: 'TBD',
    notes: 'Gap analysis to be completed in conjunction with SOC 2',
  },
  {
    standard: 'HIPAA',
    status: 'Not applicable',
    eta: 'N/A',
    notes: 'Service is not designed for or marketed to process Protected Health Information (PHI)',
  },
  {
    standard: 'PCI DSS',
    status: 'Not applicable (SAQ A)',
    eta: 'N/A',
    notes: 'Card data processed entirely by Stripe; we never see card numbers (SAQ A scope)',
  },
];

export default function SecurityPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Security at {BRAND.name}</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      <p>
        Security is foundational to {BRAND.name}. Our privacy-first architecture means that your
        documents are processed transiently — auto-deleted within 60 minutes, never stored
        long-term, and processed in sandboxed workers with no internet access. This page describes
        our technical and organisational security controls.
      </p>

      <nav aria-label="Table of contents">
        <ol className="text-sm">
          <li><a href="#architecture">1. Architecture Overview</a></li>
          <li><a href="#encryption">2. Encryption</a></li>
          <li><a href="#access-control">3. Access Control</a></li>
          <li><a href="#network">4. Network Security</a></li>
          <li><a href="#logging">5. Logging and Monitoring</a></li>
          <li><a href="#vuln-mgmt">6. Vulnerability Management</a></li>
          <li><a href="#incident-response">7. Incident Response</a></li>
          <li><a href="#backup-dr">8. Backup and Disaster Recovery</a></li>
          <li><a href="#subprocessor-security">9. Subprocessor Security</a></li>
          <li><a href="#data-residency">10. Data Residency</a></li>
          <li><a href="#compliance">11. Compliance Roadmap</a></li>
          <li><a href="#disclosure">12. Responsible Disclosure</a></li>
          <li><a href="#contact">13. Security Contact</a></li>
        </ol>
      </nav>

      <section id="architecture">
        <h2>1. Architecture Overview</h2>
        <p>
          {BRAND.name} is built on a <strong>privacy-first, file-lifecycle architecture</strong>.
          PDF files are uploaded directly to AWS S3 via pre-signed URLs — they never pass through
          our application servers. Processing workers are ephemeral containers running in sandboxed
          Kubernetes pods on Amazon EKS, with read-only filesystems, all Linux capabilities
          dropped, and zero outbound internet access. Files are tagged with an S3 object lifecycle
          rule that triggers <strong>automatic, irreversible deletion within 60 minutes</strong> of
          upload, regardless of whether processing completed successfully.
        </p>
        <p>
          Our backend uses a microservices architecture with per-service Postgres schemas and no
          cross-service database reads. Asynchronous PDF operations are queued via RabbitMQ and
          processed by KEDA-autoscaled workers. The result: a minimal blast radius if any single
          component is compromised.
        </p>
      </section>

      <section id="encryption">
        <h2>2. Encryption</h2>
        <ul>
          <li>
            <strong>In transit:</strong> All client-server and service-to-service communication is
            encrypted using <strong>TLS 1.3</strong> (minimum TLS 1.2 for legacy compatibility,
            enforced via Cloudflare). HTTP Strict Transport Security (HSTS) is enforced with a
            2-year max-age and is submitted to HSTS preload lists. Plaintext HTTP is rejected at
            the edge.
          </li>
          <li>
            <strong>At rest — files:</strong> S3 objects (uploaded files, processed outputs) are
            encrypted using <strong>AES-256 via AWS KMS</strong>-managed keys (SSE-KMS). Each
            customer&apos;s objects are encrypted with a customer-specific data key. Enterprise
            customers may supply their own AWS KMS key (Customer-Managed Keys / CMK) —
            contact <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>.
          </li>
          <li>
            <strong>At rest — database:</strong> Amazon RDS (PostgreSQL 16) storage is encrypted
            using AES-256 via AWS-managed KMS keys, applied at the volume level.
          </li>
          <li>
            <strong>At rest — EBS:</strong> All EC2 instance EBS volumes are encrypted with
            AES-256 via AWS KMS.
          </li>
          <li>
            <strong>Secrets:</strong> Application secrets (database credentials, API keys) are
            stored in AWS Secrets Manager, not in environment variables or source control. Secrets
            are rotated automatically where supported.
          </li>
        </ul>
      </section>

      <section id="access-control">
        <h2>3. Access Control</h2>
        <ul>
          <li>
            <strong>Role-based access control (RBAC):</strong> All internal access to production
            systems is governed by RBAC with the principle of least privilege. Employees are
            granted only the minimum permissions required for their role.
          </li>
          <li>
            <strong>MFA mandatory:</strong> Multi-factor authentication (MFA/TOTP) is mandatory
            for all engineers and administrators accessing production systems.
          </li>
          <li>
            <strong>SSO + JIT provisioning:</strong> Internal access is provisioned via Single
            Sign-On (SSO) with just-in-time (JIT) access for privileged operations, minimising
            standing access to sensitive systems.
          </li>
          <li>
            <strong>No shared accounts:</strong> Shared or generic service accounts are prohibited.
            Every action in production is attributable to a named individual.
          </li>
          <li>
            <strong>Offboarding:</strong> Access to all production systems is revoked within 24
            hours of employment or contractor termination.
          </li>
          <li>
            <strong>User authentication:</strong> Customer authentication is handled by Clerk,
            which supports password (bcrypt-hashed), OAuth (Google, GitHub), and TOTP MFA. We
            strongly encourage all users to enable MFA in their account settings.
          </li>
        </ul>
      </section>

      <section id="network">
        <h2>4. Network Security</h2>
        <ul>
          <li>
            <strong>VPC architecture:</strong> All services run in a private VPC. PDF processing
            workers are in private subnets with no direct internet routing. Outbound traffic from
            workers is blocked by default; only explicitly allowlisted egress (e.g., to S3 VPC
            endpoints) is permitted.
          </li>
          <li>
            <strong>Network policies:</strong> Kubernetes NetworkPolicy rules enforce
            &quot;default-deny&quot; — pods can only communicate with explicitly allowlisted peers.
            This limits lateral movement in a breach scenario.
          </li>
          <li>
            <strong>NACLs and security groups:</strong> AWS Network ACLs and security groups are
            applied at the subnet and instance level, providing defence-in-depth.
          </li>
          <li>
            <strong>Cloudflare edge:</strong> All public traffic passes through Cloudflare&apos;s
            WAF and DDoS mitigation layer before reaching our origin. Rate limiting is enforced at
            the edge for all API endpoints. Cloudflare Bot Management is enabled.
          </li>
          <li>
            <strong>NAT-only outbound:</strong> Worker containers egress only via NAT gateway for
            allowlisted destinations (e.g., S3 API, RabbitMQ). There is no general internet
            access from workers.
          </li>
        </ul>
      </section>

      <section id="logging">
        <h2>5. Logging and Monitoring</h2>
        <ul>
          <li>
            <strong>Centralised telemetry:</strong> All services emit traces, metrics, and logs via{' '}
            <strong>OpenTelemetry</strong>, collected and analysed in <strong>Grafana Cloud</strong>.
            Dashboards and alerts are maintained for API latency, error rates, queue depths, and
            security events.
          </li>
          <li>
            <strong>Immutable audit logs:</strong> Privileged actions (admin logins, data exports,
            configuration changes, data access) are recorded in immutable audit logs stored in
            S3 with object lock enabled. Logs are retained for 1 year.
          </li>
          <li>
            <strong>Security alerting:</strong> Automated alerts fire to our on-call rotation and{' '}
            <a href={`mailto:${BRAND.securityEmail}`}>{BRAND.securityEmail}</a> for anomalous
            events (e.g., unusual data access volumes, authentication spikes, new IAM privilege
            escalations).
          </li>
          <li>
            <strong>Error monitoring:</strong> Application errors are captured by Sentry with PII
            scrubbing enabled. Stack traces are reviewed by engineering; no customer file content
            is included in error reports.
          </li>
        </ul>
      </section>

      <section id="vuln-mgmt">
        <h2>6. Vulnerability Management</h2>
        <ul>
          <li>
            <strong>Dependency scanning:</strong> GitHub Dependabot runs daily scans across all
            repositories and automatically opens pull requests for patch-level dependency updates.
            Critical CVEs trigger immediate triage.
          </li>
          <li>
            <strong>Static analysis (SAST):</strong> CodeQL runs on every pull request, scanning
            for common vulnerability patterns (injection, path traversal, credential leaks). PRs
            that introduce findings are blocked from merging.
          </li>
          <li>
            <strong>Container image scanning:</strong> Container images are scanned for known CVEs
            before deployment using [TODO: tool — e.g., Trivy, Grype, or Amazon ECR native
            scanning]. Critical or high-severity findings block deployment.
          </li>
          <li>
            <strong>Penetration testing:</strong> We conduct an annual third-party penetration test
            of the web application and API surface. The most recent test was completed on [TODO:
            date]. Summary findings are available to enterprise customers under NDA.
          </li>
          <li>
            <strong>Bug bounty:</strong> See Section 12 (Responsible Disclosure). Significant
            valid findings may be recognised with a discretionary reward. A formal programme
            (e.g., HackerOne, Bugcrowd) is on our roadmap for 2026.
          </li>
        </ul>
      </section>

      <section id="incident-response">
        <h2>7. Incident Response</h2>
        <ul>
          <li>
            <strong>On-call rotation:</strong> Engineering and security on-call coverage is 24/7,
            365. On-call engineers are paged automatically via [TODO: PagerDuty/OpsGenie] for
            P0/P1 alerts.
          </li>
          <li>
            <strong>Severity matrix:</strong>
            <ul>
              <li>P0 (Critical): Confirmed data breach or service unavailability affecting all users — response within 15 minutes.</li>
              <li>P1 (High): Suspected breach, significant service degradation — response within 1 hour.</li>
              <li>P2 (Medium): Partial service degradation, non-PII security issue — response within 4 hours.</li>
              <li>P3 (Low): Minor issue, no immediate risk — response within 24 hours.</li>
            </ul>
          </li>
          <li>
            <strong>Customer notification for PII breaches:</strong> Where we confirm that a
            Personal Data Breach has affected customer data, we will notify affected Controllers
            within <strong>72 hours</strong> of confirmation, per GDPR Art. 33(2) and our DPA
            (Section 9). Notification includes the nature of the breach, data categories affected,
            and steps taken. Customers will also be notified via email and in-app notification.
          </li>
          <li>
            <strong>Post-incident review:</strong> Every P0/P1 incident triggers a blameless
            post-incident review (PIR) with a written report published internally within 5 business
            days. Summaries are shared with affected enterprise customers on request.
          </li>
          <li>
            <strong>Service status:</strong> Incidents are published to{' '}
            <a
              href={`https://status.${BRAND.domain}`}
              target="_blank"
              rel="noopener noreferrer"
            >
              status.{BRAND.domain}
            </a>{' '}
            in real time. Subscribe for email or webhook notifications.
          </li>
        </ul>
      </section>

      <section id="backup-dr">
        <h2>8. Backup and Disaster Recovery</h2>
        <ul>
          <li>
            <strong>RDS Point-in-Time Recovery (PITR):</strong> Enabled with a 35-day retention
            window. Automated snapshots taken daily.
          </li>
          <li>
            <strong>Multi-AZ failover:</strong> RDS is deployed in Multi-AZ mode. Failover to the
            standby replica is automatic, typically completing in 60–120 seconds.
          </li>
          <li>
            <strong>Recovery objectives:</strong> RPO ≤ 5 minutes; RTO ≤ 1 hour for the primary
            database. Application layer RTO ≤ 30 minutes via Kubernetes rolling deployments.
          </li>
          <li>
            <strong>Restore drills:</strong> Database restore drills are conducted quarterly to
            validate our recovery process and meet the RTO/RPO targets.
          </li>
          <li>
            <strong>S3 durability:</strong> AWS S3 provides 99.999999999% (11 nines) object
            durability via redundant storage across multiple AZs within a region.
          </li>
          <li>
            <strong>Note on uploaded files:</strong> Uploaded files are intentionally ephemeral
            (deleted within 60 minutes). We do not back up uploaded files — this is a deliberate
            privacy design decision. Please keep originals on your own storage.
          </li>
        </ul>
      </section>

      <section id="subprocessor-security">
        <h2>9. Subprocessor Security</h2>
        <p>
          We evaluate all sub-processors for security posture before engagement and annually
          thereafter using the following criteria:
        </p>
        <ul>
          <li>
            Current SOC 2 Type II report (or ISO 27001 certification as equivalent) — requested
            for all processors handling personal data.
          </li>
          <li>SIG Lite (Standard Information Gathering) questionnaire for processors without certifications.</li>
          <li>Review of publicly available security documentation and incident history.</li>
          <li>Contractual DPA with equivalent data protection obligations.</li>
        </ul>
        <p>
          The current list of approved sub-processors is at{' '}
          <a href="/legal/subprocessors">/legal/subprocessors</a>.
        </p>
      </section>

      <section id="data-residency">
        <h2>10. Data Residency</h2>
        <ul>
          <li>
            <strong>Default:</strong> Account data and uploaded files are primarily processed in
            AWS <strong>us-east-1</strong> (Virginia, USA).
          </li>
          <li>
            <strong>EU routing:</strong> EU-origin traffic is routed to <strong>eu-west-1</strong>{' '}
            (Ireland) for PDF processing where technically feasible.
          </li>
          <li>
            <strong>EU residency roadmap:</strong> Full data residency in eu-west-1 for EU
            customers — including account data and database — is on our roadmap. [TODO: Confirm
            timeline — targeted for [year]. Enterprise customers can request early access.]
          </li>
          <li>
            <strong>Region selection at signup:</strong> Enterprise plan customers will be able to
            select their preferred AWS region at account creation time.
          </li>
        </ul>
      </section>

      <section id="compliance">
        <h2>11. Compliance Roadmap</h2>
        <div className="not-prose overflow-x-auto">
          <table className="w-full border-collapse text-sm">
            <caption className="mb-2 text-left text-xs text-neutral-500">
              Compliance status as of {LAST_UPDATED}
            </caption>
            <thead>
              <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
                <th className="py-2 pr-4 font-semibold">Standard</th>
                <th className="py-2 pr-4 font-semibold">Status</th>
                <th className="py-2 pr-4 font-semibold">ETA</th>
                <th className="py-2 font-semibold">Notes</th>
              </tr>
            </thead>
            <tbody>
              {COMPLIANCE.map((row) => (
                <tr
                  key={row.standard}
                  className="border-b border-neutral-100 dark:border-neutral-800"
                >
                  <td className="py-2 pr-4 font-medium">{row.standard}</td>
                  <td className="py-2 pr-4">
                    <span
                      className={`inline-block rounded px-2 py-0.5 text-xs font-medium ${
                        row.status === 'Implemented'
                          ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
                          : row.status === 'In progress'
                          ? 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200'
                          : row.status === 'Planned'
                          ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200'
                          : 'bg-neutral-100 text-neutral-700 dark:bg-neutral-800 dark:text-neutral-300'
                      }`}
                    >
                      {row.status}
                    </span>
                  </td>
                  <td className="py-2 pr-4 text-sm">{row.eta}</td>
                  <td className="py-2 text-neutral-600 dark:text-neutral-400 text-xs">
                    {row.notes}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <p className="mt-4">
          Compliance reports and security questionnaire responses are available to enterprise
          customers and prospective customers (under NDA) on request. Email{' '}
          <a href={`mailto:${BRAND.securityEmail}`}>{BRAND.securityEmail}</a>.
        </p>
      </section>

      <section id="disclosure">
        <h2>12. Responsible Disclosure</h2>
        <p>
          We believe in the security research community and maintain a responsible disclosure
          policy with a safe harbour for good-faith security research.
        </p>
        <p>
          <strong>Safe harbour:</strong> We will not pursue civil or criminal legal action against
          researchers who discover and report security vulnerabilities in good faith, provided that
          the researcher:
        </p>
        <ul>
          <li>Does not access, modify, or exfiltrate data beyond what is necessary to demonstrate the vulnerability.</li>
          <li>Does not perform denial-of-service attacks or disrupt the service for other users.</li>
          <li>Does not target {BRAND.name} personnel or customers.</li>
          <li>Reports the vulnerability to us before public disclosure and gives us reasonable time to remediate.</li>
        </ul>
        <p>How to report:</p>
        <ul>
          <li>
            <strong>Email:</strong>{' '}
            <a href={`mailto:${BRAND.securityEmail}`}>{BRAND.securityEmail}</a>
          </li>
          <li>
            <strong>PGP key:</strong>{' '}
            <a href={`https://${BRAND.domain}/.well-known/pgp-key.txt`}>
              {BRAND.domain}/.well-known/pgp-key.txt
            </a>{' '}
            [TODO: add PGP public key before launch]
          </li>
          <li>
            <strong>security.txt (RFC 9116):</strong>{' '}
            <a href="/security.txt">{BRAND.domain}/security.txt</a>
          </li>
        </ul>
        <p>
          <strong>Response commitments:</strong> We will acknowledge your report within{' '}
          <strong>48 hours</strong> and provide a remediation timeline within{' '}
          <strong>10 business days</strong>. We aim to fix critical vulnerabilities within 30 days
          and will notify you when the fix is deployed. We may credit you in our security
          acknowledgements with your permission.
        </p>
        <p>
          <strong>Formal bug bounty:</strong> A managed bug bounty programme (e.g., via HackerOne
          or Bugcrowd) is on our 2026 roadmap. Significant valid reports may receive discretionary
          recognition in the interim.
        </p>
      </section>

      <section id="contact">
        <h2>13. Security Contact</h2>
        <address className="not-italic">
          <strong>Security team:</strong>{' '}
          <a href={`mailto:${BRAND.securityEmail}`}>{BRAND.securityEmail}</a>
          <br />
          <strong>Privacy / DPO:</strong>{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>
          <br />
          <strong>Support:</strong>{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>
          <br />
          <strong>Registered address:</strong> {BRAND.legalAddress}
        </address>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
