import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Cookie Policy',
  description: `Cookie Policy for ${BRAND.name}. Learn which cookies we set and how to manage your preferences.`,
};

const LAST_UPDATED = '2026-05-16';

interface CookieRow {
  name: string;
  provider: string;
  purpose: string;
  type: 'Essential' | 'Analytics' | 'Marketing';
  duration: string;
}

const COOKIES: CookieRow[] = [
  {
    name: '__session',
    provider: 'Clerk',
    purpose: 'Authenticated user session token. Required for login to work.',
    type: 'Essential',
    duration: 'Session',
  },
  {
    name: '__client_uat',
    provider: 'Clerk',
    purpose: 'Clerk client update token for session hydration.',
    type: 'Essential',
    duration: '1 year',
  },
  {
    name: 'cookie_consent',
    provider: BRAND.name,
    purpose:
      'Records your cookie consent preference (essential-only or all). Required to honour your choice.',
    type: 'Essential',
    duration: '1 year',
  },
  {
    name: '_ga',
    provider: 'Google Analytics (if opted in)',
    purpose: 'Distinguishes unique users for aggregate analytics.',
    type: 'Analytics',
    duration: '2 years',
  },
  {
    name: '_ga_*',
    provider: 'Google Analytics (if opted in)',
    purpose: 'Persists session state for Google Analytics 4.',
    type: 'Analytics',
    duration: '2 years',
  },
];

const typeColor: Record<CookieRow['type'], string> = {
  Essential: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
  Analytics: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
  Marketing: 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200',
};

export default function CookiePolicyPage() {
  return (
    <article className="prose prose-neutral dark:prose-invert max-w-none">
      {/* Template Notice */}
      <div className="not-prose mb-8 rounded-lg border border-amber-300 bg-amber-50 p-4 dark:border-amber-700 dark:bg-amber-950">
        <p className="text-sm font-semibold text-amber-800 dark:text-amber-200">
          TEMPLATE NOTICE: This document is a starting template — you MUST have it reviewed by a
          qualified lawyer before deployment. {BRAND.name} team has not provided legal advice.
        </p>
      </div>

      <h1>Cookie Policy</h1>
      <p className="text-sm text-neutral-500">Last updated: {LAST_UPDATED}</p>

      <p>
        This Cookie Policy explains what cookies are, which cookies {BRAND.name} sets, and how you
        can manage your preferences. For broader privacy practices, see our{' '}
        <a href="/legal/privacy">Privacy Policy</a>.
      </p>

      <h2>What Are Cookies?</h2>
      <p>
        Cookies are small text files stored in your browser when you visit a website. They are
        widely used to make websites work correctly, remember your preferences, and provide
        analytics to site owners.
      </p>

      <h2>Cookies We Set</h2>
      <div className="not-prose overflow-x-auto">
        <table className="w-full border-collapse text-sm">
          <thead>
            <tr className="border-b border-neutral-200 dark:border-neutral-700 text-left">
              <th className="py-2 pr-4 font-semibold">Cookie</th>
              <th className="py-2 pr-4 font-semibold">Provider</th>
              <th className="py-2 pr-4 font-semibold">Purpose</th>
              <th className="py-2 pr-4 font-semibold">Type</th>
              <th className="py-2 font-semibold">Duration</th>
            </tr>
          </thead>
          <tbody>
            {COOKIES.map((cookie) => (
              <tr
                key={cookie.name}
                className="border-b border-neutral-100 dark:border-neutral-800"
              >
                <td className="py-2 pr-4 font-mono text-xs">{cookie.name}</td>
                <td className="py-2 pr-4">{cookie.provider}</td>
                <td className="py-2 pr-4">{cookie.purpose}</td>
                <td className="py-2 pr-4">
                  <span
                    className={`inline-block rounded px-2 py-0.5 text-xs font-medium ${typeColor[cookie.type]}`}
                  >
                    {cookie.type}
                  </span>
                </td>
                <td className="py-2">{cookie.duration}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <h2>Managing Your Preferences</h2>
      <p>
        When you first visit {BRAND.name}, you will see a cookie consent banner. You can choose
        &quot;Accept all&quot; to enable all cookies, or &quot;Essentials only&quot; to limit
        cookies to those strictly necessary for the service to function.
      </p>
      <p>
        You can change your preferences at any time by clicking &quot;Manage cookies&quot; in the
        site footer.
      </p>
      <p>
        You can also control cookies through your browser settings. Note that disabling essential
        cookies will prevent login from working correctly.
      </p>

      <h2>Third-Party Cookies</h2>
      <p>
        Analytics cookies (when consented) are set by Google Analytics. Google processes the data
        as described in their{' '}
        <a
          href="https://policies.google.com/privacy"
          target="_blank"
          rel="noopener noreferrer"
        >
          Privacy Policy
        </a>
        .
      </p>
      <p>
        Authentication cookies are set by Clerk. Clerk processes the data as described in their{' '}
        <a href="https://clerk.com/privacy" target="_blank" rel="noopener noreferrer">
          Privacy Policy
        </a>
        .
      </p>

      <h2>Contact</h2>
      <p>
        Questions about our cookie practices? Contact{' '}
        <a href={`mailto:${BRAND.dpoEmail}`}>{BRAND.dpoEmail}</a>.
      </p>
    </article>
  );
}
