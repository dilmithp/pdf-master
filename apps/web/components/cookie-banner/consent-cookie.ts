import type { ConsentPreferences } from './types';

const COOKIE_NAME = 'cookie_consent';
const MAX_AGE_SECONDS = 365 * 24 * 60 * 60; // 1 year

export function readConsentCookie(): ConsentPreferences | null {
  if (typeof document === 'undefined') return null;
  const match = document.cookie
    .split('; ')
    .find((row) => row.startsWith(`${COOKIE_NAME}=`));
  if (!match) return null;
  try {
    const value = match.split('=')[1];
    return value ? (JSON.parse(decodeURIComponent(value)) as ConsentPreferences) : null;
  } catch {
    return null;
  }
}

export function writeConsentCookie(prefs: ConsentPreferences): void {
  if (typeof document === 'undefined') return;
  const encoded = encodeURIComponent(JSON.stringify(prefs));
  document.cookie = [
    `${COOKIE_NAME}=${encoded}`,
    `max-age=${MAX_AGE_SECONDS}`,
    'path=/',
    'SameSite=Lax',
    window.location.protocol === 'https:' ? 'Secure' : '',
  ]
    .filter(Boolean)
    .join('; ');
}
