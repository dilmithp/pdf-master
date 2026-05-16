'use client';

import { createContext, useContext } from 'react';
import type { ConsentContextValue } from './types';

export const ConsentContext = createContext<ConsentContextValue | null>(null);

export function useConsent(): ConsentContextValue {
  const ctx = useContext(ConsentContext);
  if (!ctx) {
    throw new Error('useConsent must be used within a CookieBannerProvider');
  }
  return ctx;
}
