'use client';

import { type ReactNode, useEffect, useState } from 'react';
import { CookieBanner } from './CookieBanner';
import { ConsentContext } from './ConsentContext';
import { readConsentCookie, writeConsentCookie } from './consent-cookie';
import type { ConsentPreferences } from './types';

interface CookieBannerProviderProps {
  children: ReactNode;
}

export function CookieBannerProvider({ children }: CookieBannerProviderProps) {
  const [consent, setConsentState] = useState<ConsentPreferences | null>(null);
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    const saved = readConsentCookie();
    setConsentState(saved);
    setMounted(true);
  }, []);

  function setConsent(prefs: ConsentPreferences): void {
    writeConsentCookie(prefs);
    setConsentState(prefs);
  }

  function handleAcceptAll(): void {
    setConsent({ essential: true, analytics: true, marketing: true });
  }

  function handleEssentialOnly(): void {
    setConsent({ essential: true, analytics: false, marketing: false });
  }

  const showBanner = mounted && consent === null;

  return (
    <ConsentContext.Provider
      value={{ consent, setConsent, hasConsented: consent !== null }}
    >
      {children}
      {showBanner && (
        <CookieBanner
          onAcceptAll={handleAcceptAll}
          onEssentialOnly={handleEssentialOnly}
        />
      )}
    </ConsentContext.Provider>
  );
}
