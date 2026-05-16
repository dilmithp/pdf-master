import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Imprint / Legal Notice',
  description: `Legal notice (Impressum) for ${BRAND.name} as required by German TMG §5, Austrian ECG §5, and equivalent EU e-commerce disclosure requirements.`,
};

const LAST_UPDATED = '2026-05-16';

export default function ImprintPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Imprint / Legal Notice</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED}
      </p>

      <p>
        This legal notice (Impressum) is required by German law (§ 5 Telemediengesetz, TMG),
        Austrian law (§ 5 ECG), and the EU E-Commerce Directive (2000/31/EC) for providers of
        telemedia services. If your primary operating entity is not in the EU, confirm with counsel
        whether and how this page applies to your structure.
      </p>

      <div className="not-prose mb-6 rounded-lg border border-neutral-200 bg-amber-50 p-4 dark:border-neutral-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          [TODO: All bracketed fields below must be completed with accurate legal information
          before this page is published. Inaccurate Imprint information may result in fines
          under German TMG or equivalent national law.]
        </p>
      </div>

      <section>
        <h2>Service Provider</h2>
        <p>
          The following entity is responsible for the content of this website pursuant to § 5 TMG
          (Germany), § 5 ECG (Austria), and equivalent EU regulations:
        </p>
        <address className="not-italic">
          <strong>{BRAND.companyEntity}</strong>
          <br />
          {BRAND.legalAddress}
          <br />
          [TODO: Replace with actual legal registered address]
        </address>
      </section>

      <section>
        <h2>Contact</h2>
        <ul>
          <li>
            <strong>General enquiries:</strong>{' '}
            <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>
          </li>
          <li>
            <strong>Legal / privacy:</strong>{' '}
            <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>
          </li>
          <li>
            <strong>Telephone:</strong> [TODO: Add phone number — required for TMG §5(1)(2). A
            virtual number or VoIP number is acceptable provided it connects to a human or
            voicemail that is monitored regularly.]
          </li>
          <li>
            <strong>Fax:</strong> [TODO: Optional — add if applicable]
          </li>
        </ul>
      </section>

      <section>
        <h2>Company Register</h2>
        <ul>
          <li>
            <strong>Legal form:</strong> [TODO: e.g., Inc. (Delaware corporation), GmbH, Ltd. —
            specify the legal form of the entity responsible for EU operations]
          </li>
          <li>
            <strong>Register / Court:</strong> [TODO: e.g., &quot;Registered in Delaware, USA,
            File No. [number]&quot; or &quot;Handelsregister München, HRB [number]&quot; for a
            German GmbH]
          </li>
          <li>
            <strong>Registration number:</strong> [TODO: Add company registration / Handelsregisternummer]
          </li>
        </ul>
      </section>

      <section>
        <h2>VAT Identification Number</h2>
        <p>
          [TODO: Add EU VAT ID if applicable — required by TMG §5(1)(6) for commercial service
          providers. Format: DE[9 digits] for German entity, or the VAT ID of your EU operating
          entity. If you are a US-only entity without an EU establishment, confirm with counsel
          whether OSS (One-Stop-Shop) VAT registration in an EU member state applies.]
        </p>
        <p>
          VAT identification number pursuant to § 27a UStG (Germany) / Art. 214 VAT Directive:
          [TODO: e.g., DE 123 456 789]
        </p>
      </section>

      <section>
        <h2>Responsible Person for Editorial Content</h2>
        <p>
          The person responsible for editorial content pursuant to § 55 Abs. 2 RStV (Germany):
        </p>
        <address className="not-italic">
          [TODO: Name and address of the responsible natural person — typically the CEO or managing
          director. Required for websites with journalistic/editorial content. May not be required
          for pure e-commerce/SaaS sites without editorial content, but best practice to include.]
          <br />
          [Name]
          <br />
          [Address — may be the company address]
        </address>
      </section>

      <section>
        <h2>EU Representative (GDPR Art. 27)</h2>
        <p>
          If {BRAND.companyEntity} is not established in the EU but offers services to EU residents,
          a GDPR Art. 27 representative must be designated:
        </p>
        <address className="not-italic">
          [TODO: Name and address of the EU GDPR representative. Consider using a service such as
          EDPO (edpo.com) or VeraSafe (verasafe.com). The representative&apos;s contact details
          must be published here and in the Privacy Policy.]
        </address>
      </section>

      <section>
        <h2>UK Representative (UK GDPR Art. 27)</h2>
        <p>
          [TODO: If {BRAND.name} offers services to UK residents without a UK establishment, appoint
          a UK GDPR Art. 27 representative. Contact details must be published here.]
        </p>
      </section>

      <section>
        <h2>Professional Regulatory Information</h2>
        <p>
          [TODO: Complete this section if {BRAND.name} operates in a regulated sector (e.g.,
          financial services, legal, health). For a general SaaS tool, this section may be
          omitted — confirm with counsel.]
        </p>
      </section>

      <section>
        <h2>Dispute Resolution</h2>
        <p>
          The European Commission provides an Online Dispute Resolution (ODR) platform for consumer
          disputes. As a service provider, we are required to provide a link to this platform:
        </p>
        <p>
          <a href="https://ec.europa.eu/consumers/odr" target="_blank" rel="noopener noreferrer">
            ec.europa.eu/consumers/odr
          </a>
        </p>
        <p>
          Our contact address for ODR purposes:{' '}
          <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>
        </p>
        <p>
          [TODO: Confirm whether {BRAND.companyEntity} is obliged to participate in consumer
          arbitration proceedings (Verbraucherstreitbeilegungsgesetz / VSBG in Germany). If you
          are not obliged and do not participate voluntarily, state: &quot;We are not obliged to
          participate in consumer dispute resolution proceedings and do not voluntarily participate.&quot;
          — required disclosure per § 36 VSBG.]
        </p>
        <p>
          We are not obliged or willing to participate in consumer arbitration proceedings pursuant
          to § 36 VSBG (Germany). [TODO: Confirm and update this statement with counsel.]
        </p>
      </section>

      <section>
        <h2>Liability for Content</h2>
        <p>
          As a service provider, we are responsible for our own content on these pages in accordance
          with § 7 Abs. 1 TMG under general law. Under §§ 8–10 TMG, however, we are not obliged
          as a service provider to monitor transmitted or stored third-party information or to
          investigate circumstances that indicate illegal activity. Obligations to remove or block
          the use of information in accordance with general laws remain unaffected by this. Any
          liability in this regard is only possible from the date of knowledge of a specific
          infringement of law.
        </p>
      </section>

      <section>
        <h2>Liability for Links</h2>
        <p>
          Our offer contains links to external third-party websites over whose content we have no
          influence. Therefore, we cannot accept any liability for these external contents. The
          respective provider or operator of the linked pages is always responsible for the content
          of those pages. The linked pages were checked for possible legal violations at the time
          of linking. Illegal content was not discernible at the time of linking. Permanent
          monitoring of linked pages is not reasonable without concrete indication of an
          infringement. If we become aware of any legal violations, we will remove such links
          immediately.
        </p>
      </section>

      <section>
        <h2>Copyright</h2>
        <p>
          The content and works created by the site operators on these pages are subject to
          applicable copyright law. Duplication, processing, distribution, or any form of
          commercialisation of such material beyond the scope of the copyright law requires the
          prior written consent of its respective author or creator. Downloads and copies of this
          site are only permitted for private, non-commercial use.
        </p>
        <p>
          Insofar as content on this site was not created by the operator, the copyrights of third
          parties are respected. In particular, third-party content is marked as such. Should you
          become aware of a copyright infringement, please inform us accordingly. If we become
          aware of any legal violations, we will remove such content immediately.
        </p>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
