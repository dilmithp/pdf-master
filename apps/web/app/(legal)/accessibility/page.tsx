import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Accessibility Statement',
  description: `Accessibility Statement for ${BRAND.name}. Our commitment to WCAG 2.1 AA, known gaps, testing methodology, and how to request accommodations.`,
};

const LAST_UPDATED = '2026-05-16';

interface KnownGap {
  area: string;
  description: string;
  priority: 'High' | 'Medium' | 'Low';
  targetFix: string;
}

const KNOWN_GAPS: KnownGap[] = [
  {
    area: 'PDF canvas viewer',
    description:
      'The interactive PDF rendering canvas does not yet expose a full ARIA description of page content. Screen readers cannot read the text of the rendered PDF from the canvas element.',
    priority: 'High',
    targetFix: 'Q3 2026 — adding text-layer overlay for screen reader access',
  },
  {
    area: 'Drag-and-drop file upload',
    description:
      'Drag-and-drop upload zones currently lack keyboard-equivalent actions for all gesture interactions. A click-to-upload fallback exists but some advanced gestures are mouse-only.',
    priority: 'Medium',
    targetFix: 'Q3 2026',
  },
  {
    area: 'AI chat interface',
    description:
      'Live region announcements for streaming AI responses are not yet implemented. Screen reader users may not be automatically notified when a response arrives.',
    priority: 'Medium',
    targetFix: 'Q4 2026',
  },
  {
    area: 'Colour contrast — some secondary UI elements',
    description:
      'Certain secondary action buttons and placeholder text in the light theme do not yet meet the WCAG 2.1 AA 4.5:1 contrast ratio for normal text. Identified via automated audit.',
    priority: 'Low',
    targetFix: 'Q3 2026 — design system token audit in progress',
  },
];

export default function AccessibilityPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Accessibility Statement</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      <nav aria-label="Table of contents">
        <ol className="text-sm">
          <li><a href="#commitment">1. Our Commitment</a></li>
          <li><a href="#conformance">2. Conformance Status</a></li>
          <li><a href="#technical">3. Technical Specification</a></li>
          <li><a href="#known-gaps">4. Known Gaps and Remediation Plan</a></li>
          <li><a href="#testing">5. Testing Methodology</a></li>
          <li><a href="#feedback">6. Feedback and Accommodation Requests</a></li>
          <li><a href="#enforcement">7. Enforcement Procedure</a></li>
        </ol>
      </nav>

      <section id="commitment">
        <h2>1. Our Commitment</h2>
        <p>
          {BRAND.companyEntity} is committed to ensuring that {BRAND.name} is accessible to all
          users, including people with disabilities. We aim to meet{' '}
          <strong>Web Content Accessibility Guidelines (WCAG) 2.1 Level AA</strong> across all
          pages and features of our platform.
        </p>
        <p>
          We believe accessibility is a fundamental aspect of usability, not an afterthought.
          We incorporate accessibility review into our design and development process and treat
          accessibility bugs with the same priority as functional bugs.
        </p>
      </section>

      <section id="conformance">
        <h2>2. Conformance Status</h2>
        <p>
          The Web Content Accessibility Guidelines (WCAG) defines requirements for designers and
          developers to improve accessibility for people with disabilities. It defines three levels
          of conformance: A, AA, and AAA.
        </p>
        <p>
          {BRAND.name} is <strong>partially conformant</strong> with WCAG 2.1 Level AA. Partially
          conformant means that some parts of the content do not fully conform to the accessibility
          standard. The known non-conformances are listed in Section 4 below, along with our
          remediation plan and target dates.
        </p>
        <p>
          The marketing pages at{' '}
          <a href={`https://${BRAND.domain}`}>{BRAND.domain}</a> are assessed as{' '}
          <strong>conformant</strong> with WCAG 2.1 Level AA. The web application (authenticated
          area) is partially conformant, with the gaps noted in Section 4.
        </p>
      </section>

      <section id="technical">
        <h2>3. Technical Specification</h2>
        <p>
          {BRAND.name} relies on the following technologies for conformance:
        </p>
        <ul>
          <li>HTML5 (semantic landmarks, ARIA roles)</li>
          <li>CSS3 (responsive layout, high-contrast support)</li>
          <li>JavaScript (React 19, server components)</li>
          <li>ARIA (Accessible Rich Internet Applications) specifications</li>
          <li>WAI-ARIA 1.2 for interactive components</li>
        </ul>
        <p>
          The platform is designed and tested with the following assistive technologies:
        </p>
        <ul>
          <li>VoiceOver (macOS and iOS) with Safari</li>
          <li>NVDA with Firefox (Windows)</li>
          <li>JAWS with Chrome (Windows)</li>
          <li>TalkBack with Chrome (Android)</li>
          <li>Keyboard-only navigation (Tab, Shift+Tab, Enter, Space, Arrow keys)</li>
        </ul>
      </section>

      <section id="known-gaps">
        <h2>4. Known Gaps and Remediation Plan</h2>
        <p>
          The following known non-conformances have been identified. We are actively working to
          address them:
        </p>
        <div className="not-prose overflow-x-auto">
          <table className="w-full border-collapse text-sm">
            <caption className="mb-2 text-left text-xs text-neutral-500">
              Known accessibility gaps and remediation timeline
            </caption>
            <thead>
              <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
                <th className="py-2 pr-4 font-semibold">Area</th>
                <th className="py-2 pr-4 font-semibold">Description</th>
                <th className="py-2 pr-4 font-semibold">Priority</th>
                <th className="py-2 font-semibold">Target Fix</th>
              </tr>
            </thead>
            <tbody>
              {KNOWN_GAPS.map((gap) => (
                <tr
                  key={gap.area}
                  className="border-b border-neutral-100 dark:border-neutral-800"
                >
                  <td className="py-2 pr-4 font-medium align-top">{gap.area}</td>
                  <td className="py-2 pr-4 text-neutral-600 dark:text-neutral-400 align-top text-xs">
                    {gap.description}
                  </td>
                  <td className="py-2 pr-4 align-top">
                    <span
                      className={`inline-block rounded px-2 py-0.5 text-xs font-medium ${
                        gap.priority === 'High'
                          ? 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
                          : gap.priority === 'Medium'
                          ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200'
                          : 'bg-neutral-100 text-neutral-700 dark:bg-neutral-800 dark:text-neutral-300'
                      }`}
                    >
                      {gap.priority}
                    </span>
                  </td>
                  <td className="py-2 align-top text-xs">{gap.targetFix}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <p className="mt-4">
          This list is reviewed and updated on a quarterly basis. If you encounter an accessibility
          issue not listed here, please report it (see Section 6).
        </p>
      </section>

      <section id="testing">
        <h2>5. Testing Methodology</h2>
        <p>We use the following testing approaches to assess and maintain accessibility:</p>
        <ul>
          <li>
            <strong>Automated testing:</strong> Axe-core (via Playwright accessibility tests) runs
            on every pull request and CI build, catching common WCAG violations automatically.
            Lighthouse CI runs on all marketing pages and gates merges if accessibility score drops
            below 90.
          </li>
          <li>
            <strong>Manual keyboard testing:</strong> All new interactive components are manually
            tested for keyboard navigability (tab order, focus visibility, enter/space activation)
            before release.
          </li>
          <li>
            <strong>Screen reader testing:</strong> Major user flows (sign-up, file upload, PDF
            processing, account management) are manually tested with VoiceOver on macOS and NVDA
            on Windows before each major release.
          </li>
          <li>
            <strong>Colour contrast:</strong> All colour tokens in our Tailwind v4 design system
            are checked against WCAG 2.1 4.5:1 (normal text) and 3:1 (large text) contrast ratios
            using Radix Colours and automated contrast checking tools.
          </li>
          <li>
            <strong>Third-party audit:</strong> We commission a third-party WCAG 2.1 AA
            conformance audit [TODO: annually / before major product launches]. The most recent
            audit was completed: [TODO: date]. Report available on request.
          </li>
        </ul>
      </section>

      <section id="feedback">
        <h2>6. Feedback and Accommodation Requests</h2>
        <p>
          We welcome feedback about the accessibility of {BRAND.name}. If you experience a barrier
          that prevents you from accessing any feature, or if you need content in an alternative
          format, please contact us:
        </p>
        <ul>
          <li>
            <strong>Email:</strong>{' '}
            <a href={`mailto:${BRAND.supportEmail}`}>{BRAND.supportEmail}</a>
          </li>
          <li>
            <strong>Subject line:</strong> &quot;Accessibility&quot;
          </li>
          <li>
            <strong>Response time:</strong> We aim to respond within 2 business days and to
            resolve reported issues or provide an equivalent alternative within 30 days.
          </li>
        </ul>
        <p>
          When reporting an accessibility issue, please include:
        </p>
        <ul>
          <li>The URL of the page where you encountered the barrier.</li>
          <li>A description of the issue and the specific task you were trying to complete.</li>
          <li>The assistive technology and browser you were using (if applicable).</li>
        </ul>
      </section>

      <section id="enforcement">
        <h2>7. Enforcement Procedure</h2>
        <p>
          If you are not satisfied with our response to an accessibility complaint:
        </p>
        <ul>
          <li>
            <strong>EU / EEA:</strong> The European Accessibility Act (EAA, Directive 2019/882)
            applies to digital products and services from June 2025. You may lodge a complaint with
            your national enforcement body. [TODO: confirm which national authority is responsible
            for your jurisdiction.]
          </li>
          <li>
            <strong>UK:</strong> The Equality Act 2010 requires that service providers make
            reasonable adjustments for disabled people. You may contact the Equality and Human
            Rights Commission (EHRC) at{' '}
            <a href="https://www.equalityhumanrights.com" target="_blank" rel="noopener noreferrer">
              equalityhumanrights.com
            </a>
            .
          </li>
          <li>
            <strong>USA:</strong> The Americans with Disabilities Act (ADA) applies to public
            accommodations. You may file a complaint with the U.S. Department of Justice.
          </li>
        </ul>
        <p>
          [TODO: Confirm with counsel whether the EAA applies to your product class and jurisdiction
          before EU launch.]
        </p>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
