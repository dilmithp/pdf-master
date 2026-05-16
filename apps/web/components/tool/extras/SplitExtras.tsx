"use client";

import { useState } from "react";

export type SplitMode = "range" | "every-n" | "each-page";

export interface SplitExtrasValue {
  mode: SplitMode;
  range?: string;
  everyN?: number;
}

interface SplitExtrasProps {
  onChange: (value: SplitExtrasValue) => void;
}

export function SplitExtras({ onChange }: SplitExtrasProps) {
  const [mode, setMode] = useState<SplitMode>("range");
  const [range, setRange] = useState("");
  const [everyN, setEveryN] = useState(1);

  function emit(updates: Partial<{ mode: SplitMode; range: string; everyN: number }>) {
    const next = { mode, range, everyN, ...updates };
    onChange({ mode: next.mode, range: next.range, everyN: next.everyN });
  }

  return (
    <fieldset className="space-y-3 rounded-lg border border-neutral-200 p-4 dark:border-neutral-700">
      <legend className="px-1 text-sm font-medium">Split options</legend>

      <div className="flex flex-wrap gap-3">
        {(["range", "every-n", "each-page"] as SplitMode[]).map((m) => (
          <label key={m} className="flex items-center gap-2 cursor-pointer text-sm">
            <input
              type="radio"
              name="split-mode"
              value={m}
              checked={mode === m}
              onChange={() => {
                setMode(m);
                emit({ mode: m });
              }}
            />
            {m === "range" ? "By page range" : m === "every-n" ? "Every N pages" : "Each page"}
          </label>
        ))}
      </div>

      {mode === "range" && (
        <div>
          <label htmlFor="split-range" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
            Page ranges (e.g. 1-3, 5, 7-9)
          </label>
          <input
            id="split-range"
            type="text"
            value={range}
            onChange={(e) => {
              setRange(e.target.value);
              emit({ range: e.target.value });
            }}
            placeholder="1-3, 5, 7-9"
            className="w-full rounded-md border border-neutral-300 px-3 py-1.5 text-sm dark:border-neutral-600 dark:bg-neutral-800"
          />
        </div>
      )}

      {mode === "every-n" && (
        <div>
          <label htmlFor="split-every-n" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
            Split every N pages
          </label>
          <input
            id="split-every-n"
            type="number"
            min={1}
            value={everyN}
            onChange={(e) => {
              const n = Math.max(1, Number(e.target.value));
              setEveryN(n);
              emit({ everyN: n });
            }}
            className="w-24 rounded-md border border-neutral-300 px-3 py-1.5 text-sm dark:border-neutral-600 dark:bg-neutral-800"
          />
        </div>
      )}
    </fieldset>
  );
}
