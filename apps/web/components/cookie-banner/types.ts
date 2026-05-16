export interface ConsentPreferences {
  essential: true; // always on, not user-configurable
  analytics: boolean;
  marketing: boolean;
}

export type ConsentValue = 'all' | 'essential';

export interface ConsentContextValue {
  consent: ConsentPreferences | null;
  setConsent: (prefs: ConsentPreferences) => void;
  hasConsented: boolean;
}
