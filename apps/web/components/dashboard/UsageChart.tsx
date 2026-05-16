interface UsageChartProps {
  readonly used: number;
  readonly total: number | "unlimited";
  readonly label: string;
}

export function UsageChart({ used, total, label }: UsageChartProps) {
  const pct = total === "unlimited" ? 0 : Math.min(100, Math.round((used / total) * 100));
  const isUnlimited = total === "unlimited";

  return (
    <div className="space-y-2">
      <div className="flex justify-between text-sm text-neutral-700 dark:text-neutral-300">
        <span>{label}</span>
        <span>
          {used} / {isUnlimited ? "∞" : total}
        </span>
      </div>
      <div className="h-3 overflow-hidden rounded-full bg-neutral-100 dark:bg-neutral-800">
        {!isUnlimited && (
          <div
            className="h-full rounded-full bg-blue-500 transition-all"
            style={{ width: `${pct}%` }}
            role="progressbar"
            aria-valuenow={used}
            aria-valuemax={total as number}
            aria-label={`${label}: ${used} of ${total} used`}
          />
        )}
      </div>
      {!isUnlimited && (
        <p className="text-xs text-neutral-400">
          {pct}% used — {(total as number) - used} remaining
        </p>
      )}
    </div>
  );
}
