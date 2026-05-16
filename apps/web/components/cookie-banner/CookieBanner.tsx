'use client';

import { useEffect, useRef, useState } from 'react';

interface CookieBannerProps {
  onAcceptAll: () => void;
  onEssentialOnly: () => void;
}

interface DialogPrefs {
  analytics: boolean;
  marketing: boolean;
}

export function CookieBanner({ onAcceptAll, onEssentialOnly }: CookieBannerProps) {
  const [dialogOpen, setDialogOpen] = useState(false);
  const [prefs, setPrefs] = useState<DialogPrefs>({ analytics: false, marketing: false });

  const dialogRef = useRef<HTMLDivElement>(null);
  const firstFocusRef = useRef<HTMLButtonElement>(null);

  // Focus trap within dialog
  useEffect(() => {
    if (!dialogOpen) return;
    firstFocusRef.current?.focus();

    function handleKeyDown(e: KeyboardEvent) {
      if (e.key !== 'Tab' || !dialogRef.current) return;
      const focusable = Array.from(
        dialogRef.current.querySelectorAll<HTMLElement>(
          'button, input, a[href], [tabindex]:not([tabindex="-1"])',
        ),
      ).filter((el) => !el.hasAttribute('disabled'));
      const first = focusable[0];
      const last = focusable[focusable.length - 1];
      if (!first || !last) return;
      if (e.shiftKey && document.activeElement === first) {
        e.preventDefault();
        last.focus();
      } else if (!e.shiftKey && document.activeElement === last) {
        e.preventDefault();
        first.focus();
      }
    }

    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [dialogOpen]);

  function handleSavePreferences() {
    if (prefs.analytics || prefs.marketing) {
      onAcceptAll();
    } else {
      onEssentialOnly();
    }
    setDialogOpen(false);
  }

  return (
    <>
      {/* Banner */}
      <div
        role="region"
        aria-label="Cookie consent"
        className="fixed bottom-0 left-0 right-0 z-50 border-t border-neutral-200 bg-white p-4 shadow-lg dark:border-neutral-700 dark:bg-neutral-900 sm:p-6"
      >
        <div className="mx-auto flex max-w-4xl flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <p className="text-sm text-neutral-700 dark:text-neutral-300">
            We use essential cookies to operate the site, and optional analytics cookies to
            improve it.{' '}
            <a
              href="/legal/cookies"
              className="underline hover:text-neutral-900 dark:hover:text-neutral-100"
            >
              Cookie Policy
            </a>
            .
          </p>
          <div className="flex flex-shrink-0 flex-wrap gap-2">
            <button
              type="button"
              onClick={() => setDialogOpen(true)}
              className="rounded border border-neutral-300 px-3 py-1.5 text-sm text-neutral-700 hover:bg-neutral-100 dark:border-neutral-600 dark:text-neutral-300 dark:hover:bg-neutral-800"
            >
              Manage
            </button>
            <button
              type="button"
              onClick={onEssentialOnly}
              className="rounded border border-neutral-300 px-3 py-1.5 text-sm text-neutral-700 hover:bg-neutral-100 dark:border-neutral-600 dark:text-neutral-300 dark:hover:bg-neutral-800"
            >
              Essentials only
            </button>
            <button
              type="button"
              onClick={onAcceptAll}
              className="rounded bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Accept all
            </button>
          </div>
        </div>
      </div>

      {/* Preferences Dialog */}
      {dialogOpen && (
        <div
          role="dialog"
          aria-modal="true"
          aria-labelledby="cookie-dialog-title"
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4"
        >
          <div
            ref={dialogRef}
            className="w-full max-w-md rounded-lg border border-neutral-200 bg-white p-6 shadow-xl dark:border-neutral-700 dark:bg-neutral-900"
          >
            <h2
              id="cookie-dialog-title"
              className="mb-4 text-lg font-semibold text-neutral-900 dark:text-neutral-100"
            >
              Cookie Preferences
            </h2>

            {/* Essential — always on */}
            <div className="mb-4 flex items-start gap-3">
              <input
                id="essential"
                type="checkbox"
                checked
                disabled
                aria-disabled="true"
                className="mt-1 h-4 w-4 cursor-not-allowed rounded accent-blue-600 opacity-60"
              />
              <div>
                <label htmlFor="essential" className="font-medium text-neutral-900 dark:text-neutral-100">
                  Essential cookies
                </label>
                <p className="text-sm text-neutral-500 dark:text-neutral-400">
                  Required for authentication, session management, and consent preferences.
                  Cannot be disabled.
                </p>
              </div>
            </div>

            {/* Analytics */}
            <div className="mb-4 flex items-start gap-3">
              <input
                id="analytics"
                type="checkbox"
                checked={prefs.analytics}
                onChange={(e) => setPrefs((p) => ({ ...p, analytics: e.target.checked }))}
                className="mt-1 h-4 w-4 rounded accent-blue-600"
              />
              <div>
                <label htmlFor="analytics" className="font-medium text-neutral-900 dark:text-neutral-100">
                  Analytics cookies
                </label>
                <p className="text-sm text-neutral-500 dark:text-neutral-400">
                  Help us understand which features are most useful. Data is aggregated and
                  anonymous.
                </p>
              </div>
            </div>

            {/* Marketing */}
            <div className="mb-6 flex items-start gap-3">
              <input
                id="marketing"
                type="checkbox"
                checked={prefs.marketing}
                onChange={(e) => setPrefs((p) => ({ ...p, marketing: e.target.checked }))}
                className="mt-1 h-4 w-4 rounded accent-blue-600"
              />
              <div>
                <label htmlFor="marketing" className="font-medium text-neutral-900 dark:text-neutral-100">
                  Marketing cookies
                </label>
                <p className="text-sm text-neutral-500 dark:text-neutral-400">
                  Allow us to show you relevant ads on third-party platforms.
                </p>
              </div>
            </div>

            <div className="flex justify-end gap-2">
              <button
                ref={firstFocusRef}
                type="button"
                onClick={() => setDialogOpen(false)}
                className="rounded border border-neutral-300 px-4 py-2 text-sm text-neutral-700 hover:bg-neutral-100 dark:border-neutral-600 dark:text-neutral-300 dark:hover:bg-neutral-800"
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={handleSavePreferences}
                className="rounded bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Save preferences
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
