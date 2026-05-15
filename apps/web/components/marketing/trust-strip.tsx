export function TrustStrip() {
  return (
    <ul className="mt-6 flex flex-wrap items-center justify-center gap-x-6 gap-y-2 text-sm text-neutral-600 dark:text-neutral-400">
      <li className="flex items-center gap-2">
        <span aria-hidden className="inline-block h-1.5 w-1.5 rounded-full bg-emerald-500" />
        Files deleted in 60 minutes
      </li>
      <li className="flex items-center gap-2">
        <span aria-hidden className="inline-block h-1.5 w-1.5 rounded-full bg-emerald-500" />
        GDPR-compliant
      </li>
      <li className="flex items-center gap-2">
        <span aria-hidden className="inline-block h-1.5 w-1.5 rounded-full bg-emerald-500" />
        No watermark on free tier
      </li>
      <li className="flex items-center gap-2">
        <span aria-hidden className="inline-block h-1.5 w-1.5 rounded-full bg-emerald-500" />
        No signup required
      </li>
    </ul>
  );
}
