import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Cookie Policy',
  description: `Cookie Policy for ${BRAND.name}. Concrete inventory of all cookies we set, ePrivacy compliance statement, and how to manage your preferences including Global Privacy Control.`,
};

const LAST_UPDATED = '2026-05-16';

interface CookieRow {
  name: string;
  type: 'Essential' | 'Analytics' | 'Advertising';
  purpose: string;
  duration: string;
  provider: string;
  firstOrThird: 'First-party' | 'Third-party';
}

const COOKIES: CookieRow[] = [
  {
    name: '__session',
    type: 'Essential',
    purpose: 'Authenticated user session token. Required for login to function.',
    duration: '7 days',
    provider: 'Clerk',
    firstOrThird: 'First-party',
  },
  {
    name: '__client_uat',
    type: 'Essential',
    purpose: 'Clerk client update token for session hydration and refresh.',
    duration: '1 year',
    provider: 'Clerk',
    firstOrThird: 'First-party',
  },
  {
    name: 'cookie_consent',
    type: 'Essential',
    purpose:
      'Records your cookie consent choice (essential-only or all). Required to honour your preference across visits.',
    duration: '1 year',
    provider: BRAND.name,
    firstOrThird: 'First-party',
  },
  {
    name: '__cf_bm',
    type: 'Essential',
    purpose:
      'Cloudflare bot management. Distinguishes legitimate users from automated bots to protect the service from abuse.',
    duration: '30 minutes',
    provider: 'Cloudflare',
    firstOrThird: 'Third-party',
  },
  {
    name: '_ga',
    type: 'Analytics',
    purpose:
      'Google Analytics — distinguishes unique users for aggregate, anonymised analytics. Only set with your consent.',
    duration: '13 months',
    provider: 'Google Analytics',
    firstOrThird: 'Third-party',
  },
  {
    name: '_ga_*',
    type: 'Analytics',
    purpose:
      'Google Analytics 4 — persists session state for aggregate analytics. Only set with your consent.',
    duration: '13 months',
    provider: 'Google Analytics',
    firstOrThird: 'Third-party',
  },
  {
    name: '_fbp',
    type: 'Advertising',
    purpose:
      'Meta Pixel — used to measure advertising effectiveness if Meta advertising is enabled. Only set with your explicit consent.',
    duration: '90 days',
    provider: 'Meta (Facebook)',
    firstOrThird: 'Third-party',
  },
];

const typeColor: Record<CookieRow['type'], string> = {
  Essential: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
  Analytics: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
  Advertising: 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200',
};

const partyColor: Record<CookieRow['firstOrThird'], string> = {
  'First-party': 'bg-neutral-100 text-neutral-700 dark:bg-neutral-800 dark:text-neutral-300',
  'Third-party': 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200',
};

export default function CookiePolicyPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice — MUST remain until lawyer-reviewed */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Cookie Policy</h1>
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &mdash; Print:{' '}
        <span className="italic">Ctrl/Cmd+P to save as PDF</span>
      </p>

      <p>
        This Cookie Policy explains what cookies and similar tracking technologies {BRAND.name} uses,
        why we use them, and how you can control them. For our broader privacy practices, including
        how we handle personal data collected via cookies, see our{' '}
        <a href="/legal/privacy">Privacy Policy</a>.
      </p>

      <nav aria-label="Table of contents">
        <ol className="text-sm">
          <li><a href="#what-are-cookies">1. What Are Cookies?</a></li>
          <li><a href="#legal-basis">2. Legal Basis for Cookie Use</a></li>
          <li><a href="#cookie-inventory">3. Cookie Inventory</a></li>
          <li><a href="#managing-preferences">4. Managing Your Preferences</a></li>
          <li><a href="#browser-controls">5. Browser-Level Controls</a></li>
          <li><a href="#gpc-dnt">6. Global Privacy Control and Do Not Track</a></li>
          <li><a href="#third-party-detail">7. Third-Party Cookie Details</a></li>
          <li><a href="#contact">8. Contact</a></li>
        </ol>
      </nav>

      <section id="what-are-cookies">
        <h2>1. What Are Cookies?</h2>
        <p>
          Cookies are small text files placed on your device (browser) when you visit a website.
          They are widely used to make sites function correctly, remember your preferences, measure
          usage, and serve targeted advertising.
        </p>
        <p>
          Similar technologies — including <strong>local storage</strong>, session storage, and
          pixel tags — may be used alongside or instead of cookies for equivalent purposes. This
          policy covers all such tracking technologies.
        </p>
        <p>
          We distinguish between:
        </p>
        <ul>
          <li>
            <strong>First-party cookies</strong> — set by {BRAND.domain} directly.
          </li>
          <li>
            <strong>Third-party cookies</strong> — set by third-party services embedded in the
            site (e.g., Cloudflare, Google Analytics). These third parties have their own privacy
            policies that govern how they use the data collected.
          </li>
        </ul>
      </section>

      <section id="legal-basis">
        <h2>2. Legal Basis for Cookie Use</h2>
        <p>
          We comply with the EU ePrivacy Directive (2002/58/EC, as amended) and applicable national
          implementations (e.g., UK PECR, German TTDSG).
        </p>
        <ul>
          <li>
            <strong>Essential cookies</strong> do not require your consent. They are strictly
            necessary for the website to function — without them, core features such as login and
            session management would not work. We rely on our legitimate interest in operating the
            Service (GDPR Art. 6(1)(f)) for these cookies.
          </li>
          <li>
            <strong>Analytics and advertising cookies</strong> require your freely given,
            specific, informed, and unambiguous <strong>prior consent</strong> (GDPR Art. 6(1)(a);
            ePrivacy Directive Art. 5(3)). These cookies are set only after you have accepted them
            via our consent banner. You may withdraw consent at any time — see Section 4.
          </li>
        </ul>
      </section>

      <section id="cookie-inventory">
        <h2>3. Cookie Inventory</h2>
        <p>
          The table below is our complete cookie inventory. We review and update this list whenever
          our cookie usage changes.
        </p>
        <div className="not-prose overflow-x-auto">
          <table className="w-full border-collapse text-sm">
            <caption className="mb-2 text-left text-xs text-neutral-500">
              Complete cookie inventory — last reviewed {LAST_UPDATED}
            </caption>
            <thead>
              <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
                <th className="py-2 pr-3 font-semibold">Cookie Name</th>
                <th className="py-2 pr-3 font-semibold">Type</th>
                <th className="py-2 pr-3 font-semibold">Party</th>
                <th className="py-2 pr-3 font-semibold">Purpose</th>
                <th className="py-2 pr-3 font-semibold">Duration</th>
                <th className="py-2 font-semibold">Provider</th>
              </tr>
            </thead>
            <tbody>
              {COOKIES.map((cookie) => (
                <tr
                  key={cookie.name}
                  className="border-b border-neutral-100 dark:border-neutral-800"
                >
                  <td className="py-2 pr-3 font-mono text-xs">{cookie.name}</td>
                  <td className="py-2 pr-3">
                    <span
                      className={`inline-block rounded px-2 py-0.5 text-xs font-medium ${typeColor[cookie.type]}`}
                    >
                      {cookie.type}
                    </span>
                  </td>
                  <td className="py-2 pr-3">
                    <span
                      className={`inline-block rounded px-2 py-0.5 text-xs font-medium ${partyColor[cookie.firstOrThird]}`}
                    >
                      {cookie.firstOrThird}
                    </span>
                  </td>
                  <td className="py-2 pr-3 text-neutral-600 dark:text-neutral-400 text-xs">
                    {cookie.purpose}
                  </td>
                  <td className="py-2 pr-3 text-xs">{cookie.duration}</td>
                  <td className="py-2 text-xs">{cookie.provider}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <p className="mt-3 text-sm text-neutral-500">
          Note: Analytics and advertising cookies (marked above) are only set after you have given
          your explicit consent. Essential cookies are always active.
        </p>
      </section>

      <section id="managing-preferences">
        <h2>4. Managing Your Cookie Preferences</h2>
        <p>
          When you first visit {BRAND.name}, you will see a cookie consent banner. Your options:
        </p>
        <ul>
          <li>
            <strong>Accept all:</strong> Enables essential, analytics, and advertising cookies
            (where configured).
          </li>
          <li>
            <strong>Essentials only:</strong> Enables only strictly necessary cookies. Analytics
            and advertising cookies will not be set.
          </li>
          <li>
            <strong>Manage preferences:</strong> Opens a detailed panel where you can toggle
            each category individually.
          </li>
        </ul>
        <p>
          <strong>Changing your preferences:</strong> You can update your cookie preferences at any
          time by clicking &quot;Manage cookies&quot; in the site footer. This will reopen the
          consent banner and allow you to change your selections.
        </p>
        <p>
          <strong>Withdrawing consent:</strong> Selecting &quot;Essentials only&quot; after
          previously accepting all cookies constitutes withdrawal of consent. We will stop setting
          analytics and advertising cookies from that point. Note: withdrawing consent does not
          delete cookies that were already placed; you must clear them via your browser settings
          (see Section 5).
        </p>
      </section>

      <section id="browser-controls">
        <h2>5. Browser-Level Cookie Controls</h2>
        <p>
          All major browsers allow you to view, block, or delete cookies at the browser level.
          Note that blocking essential cookies will prevent login from working correctly.
        </p>
        <ul>
          <li>
            <strong>Chrome:</strong>{' '}
            <a
              href="https://support.google.com/chrome/answer/95647"
              target="_blank"
              rel="noopener noreferrer"
            >
              support.google.com/chrome/answer/95647
            </a>
          </li>
          <li>
            <strong>Firefox:</strong>{' '}
            <a
              href="https://support.mozilla.org/en-US/kb/enhanced-tracking-protection-firefox-desktop"
              target="_blank"
              rel="noopener noreferrer"
            >
              Mozilla Enhanced Tracking Protection
            </a>
          </li>
          <li>
            <strong>Safari:</strong>{' '}
            <a
              href="https://support.apple.com/en-us/guide/safari/sfri11471/mac"
              target="_blank"
              rel="noopener noreferrer"
            >
              support.apple.com — Safari cookie settings
            </a>
          </li>
          <li>
            <strong>Edge:</strong>{' '}
            <a
              href="https://support.microsoft.com/en-us/microsoft-edge/delete-cookies-in-microsoft-edge-63947406-40ac-c3b8-57b9-2a946a29ae09"
              target="_blank"
              rel="noopener noreferrer"
            >
              Microsoft Edge cookie settings
            </a>
          </li>
          <li>
            <strong>Brave:</strong> Brave blocks third-party cookies and cross-site trackers by
            default. No additional action is needed for tracking protection.
          </li>
        </ul>
        <p>
          You can also opt out of Google Analytics specifically across all sites using the{' '}
          <a
            href="https://tools.google.com/dlpage/gaoptout"
            target="_blank"
            rel="noopener noreferrer"
          >
            Google Analytics Opt-out Browser Add-on
          </a>
          .
        </p>
      </section>

      <section id="gpc-dnt">
        <h2>6. Global Privacy Control (GPC) and Do Not Track (DNT)</h2>
        <p>
          <strong>Global Privacy Control (GPC):</strong> We recognise and honour the Global Privacy
          Control signal as required under the CCPA/CPRA for California residents. When our servers
          detect a valid GPC signal in your browser (sec-gpc: 1), we treat it as an opt-out of
          sale and sharing of personal information. In practice, we do not sell or share personal
          information for cross-context behavioural advertising, so the GPC signal will additionally
          ensure that optional analytics cookies are not set for that session even if you have
          previously consented.
        </p>
        <p>
          <strong>Do Not Track (DNT):</strong> The browser &quot;Do Not Track&quot; signal (DNT)
          does not have a legally defined standard, and we do not respond to it in a technically
          differentiated way at this time. We instead rely on the GPC signal (which has legal
          backing under CCPA/CPRA) and our consent management platform for preference signals.
          We encourage you to use GPC-compatible browsers (e.g., Brave, Firefox with GPC enabled)
          for the strongest privacy protection.
        </p>
      </section>

      <section id="third-party-detail">
        <h2>7. Third-Party Cookie Details</h2>

        <h3>Cloudflare (<code>__cf_bm</code>)</h3>
        <p>
          Cloudflare sets this cookie for bot management and security. It does not contain
          personally identifiable information and is used solely to distinguish human users from
          bots. As this cookie is essential for security, it is set without consent. Cloudflare&apos;s
          privacy policy:{' '}
          <a
            href="https://www.cloudflare.com/privacypolicy/"
            target="_blank"
            rel="noopener noreferrer"
          >
            cloudflare.com/privacypolicy
          </a>
          .
        </p>

        <h3>Clerk (<code>__session</code>, <code>__client_uat</code>)</h3>
        <p>
          Clerk handles user authentication and session management. Their cookies are essential for
          login to function. Clerk&apos;s privacy policy:{' '}
          <a href="https://clerk.com/privacy" target="_blank" rel="noopener noreferrer">
            clerk.com/privacy
          </a>
          . Clerk DPA:{' '}
          <a href="https://clerk.com/legal/dpa" target="_blank" rel="noopener noreferrer">
            clerk.com/legal/dpa
          </a>
          .
        </p>

        <h3>Google Analytics (<code>_ga</code>, <code>_ga_*</code>) — optional, consent required</h3>
        <p>
          Google Analytics is used to understand aggregate usage of the Service. We have configured
          Google Analytics with IP anonymisation enabled and have disabled data sharing with Google
          for advertising purposes. Google&apos;s privacy policy:{' '}
          <a
            href="https://policies.google.com/privacy"
            target="_blank"
            rel="noopener noreferrer"
          >
            policies.google.com/privacy
          </a>
          . These cookies are only set after you consent.
        </p>

        <h3>Meta Pixel (<code>_fbp</code>) — optional, consent required</h3>
        <p>
          If Meta advertising is active, the Meta Pixel may be used to measure conversion rates of
          advertising campaigns. This cookie is only set with your explicit consent and is not
          active by default. Meta&apos;s privacy policy:{' '}
          <a
            href="https://www.facebook.com/policy/cookies/"
            target="_blank"
            rel="noopener noreferrer"
          >
            facebook.com/policy/cookies
          </a>
          . [TODO: Confirm whether Meta Pixel is actually deployed before launch; remove if not.]
        </p>
      </section>

      <section id="contact">
        <h2>8. Contact</h2>
        <p>
          Questions about our cookie practices? Contact our Data Protection Officer at{' '}
          <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
        </p>
      </section>

      <hr />
      <p className="text-sm text-neutral-500">
        Last updated: {LAST_UPDATED} &bull; Last reviewed by counsel: <strong>TODO</strong>
      </p>
    </article>
  );
}
