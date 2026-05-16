import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Security',
  description: `Security practices at ${BRAND.name}: encryption, compliance, vulnerability disclosure, and more.`,
};

const LAST_UPDATED = '2026-05-16';

export default function SecurityPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      <h1>Security at {BRAND.name}</h1>
      <p className="text-sm text-neutral-500">Last updated: {LAST_UPDATED}</p>

      <p>
        Security is foundational to {BRAND.name}. This page summarises our security posture,
        controls, and how to report vulnerabilities.
      </p>

      <h2>Encryption</h2>
      <ul>
        <li>
          <strong>In transit:</strong> All data is transmitted over TLS 1.2+ (TLS 1.3 preferred).
          HSTS is enforced with a two-year max-age.
        </li>
        <li>
          <strong>At rest:</strong> Uploaded files are encrypted using AES-256 on AWS S3.
          Database data is encrypted using AWS RDS encryption (AES-256).
        </li>
        <li>
          <strong>File lifecycle:</strong> Uploaded files are automatically and irreversibly
          deleted from our servers within 60 minutes of upload. Files are never used to train
          AI models.
        </li>
      </ul>

      <h2>Infrastructure Security</h2>
      <ul>
        <li>
          Services run in sandboxed containers with <code>--read-only</code> filesystems,
          dropped Linux capabilities (<code>--cap-drop=ALL</code>), and no outbound egress from
          PDF processing workers.
        </li>
        <li>
          Network segmentation: PDF processing workers have no direct internet access and cannot
          reach the public internet.
        </li>
        <li>
          All infrastructure is deployed on AWS in SOC 2 Type II-certified data centres. Our own
          SOC 2 audit is in progress (target completion: [placeholder date]).
        </li>
        <li>
          Secrets are managed via AWS Secrets Manager and are never committed to source control.
        </li>
      </ul>

      <h2>Access Control</h2>
      <ul>
        <li>
          Authentication is handled by Clerk with support for multi-factor authentication (MFA).
          We strongly encourage all users to enable MFA.
        </li>
        <li>
          Internal access to production systems follows least-privilege principles and requires
          MFA.
        </li>
        <li>All internal access to customer data is logged and auditable.</li>
      </ul>

      <h2>Penetration Testing</h2>
      <p>
        We conduct annual third-party penetration tests against our web application and
        infrastructure. Summary results are available to enterprise customers under NDA upon
        request.
      </p>
      <p>[Placeholder: Last pen test completed — [date]. Next test scheduled — [date].]</p>

      <h2>SOC 2 Compliance</h2>
      <p>
        We are pursuing SOC 2 Type II certification. Our report will be available to enterprise
        customers under NDA once issued. In the interim, we are happy to share our security
        questionnaire responses.
      </p>

      <h2>Vulnerability Disclosure</h2>
      <p>
        We maintain a responsible disclosure programme. If you discover a security vulnerability
        in {BRAND.name}, please report it to us before disclosing it publicly.
      </p>
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
          [placeholder — add PGP key before launch]
        </li>
        <li>
          <strong>security.txt:</strong>{' '}
          <a href="/security.txt">{BRAND.domain}/security.txt</a> (RFC 9116)
        </li>
      </ul>
      <p>
        We aim to acknowledge reports within 48 hours and to provide a fix or mitigation plan
        within 90 days. We do not pursue legal action against researchers who follow this policy.
        We may offer recognition in our hall of fame for valid, responsibly disclosed reports.
      </p>

      <h2>Bug Bounty</h2>
      <p>
        A formal bug bounty programme is planned. In the meantime, significant and valid
        vulnerability reports may be recognised with a discretionary reward at our sole
        discretion.
      </p>

      <h2>Contact</h2>
      <address className="not-italic">
        <strong>Security team:</strong>{' '}
        <a href={`mailto:${BRAND.securityEmail}`}>{BRAND.securityEmail}</a>
        <br />
        <strong>Privacy / DPO:</strong>{' '}
        <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>
      </address>
    </article>
  );
}
