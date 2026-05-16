"use client";

import { useState } from "react";

export type CompressQuality = "high" | "medium" | "low";

export interface CompressExtrasValue {
  quality: CompressQuality;
}

interface CompressExtrasProps {
  onChange: (value: CompressExtrasValue) => void;
}

const QUALITY_OPTIONS: { value: CompressQuality; label: string; description: string }[] = [
  { value: "high", label: "High quality", description: "Smallest reduction, best visual result" },
  { value: "medium", label: "Recommended", description: "Good balance of size and quality" },
  { value: "low", label: "Maximum compression", description: "Smallest file, reduced quality" },
];

export function CompressExtras({ onChange }: CompressExtrasProps) {
  const [quality, setQuality] = useState<CompressQuality>("medium");

  function select(q: CompressQuality) {
    setQuality(q);
    onChange({ quality: q });
  }

  return (
    <fieldset className="space-y-2 rounded-lg border border-neutral-200 p-4 dark:border-neutral-700">
      <legend className="px-1 text-sm font-medium">Compression level</legend>
      <div className="flex flex-col gap-2">
        {QUALITY_OPTIONS.map(({ value, label, description }) => (
          <label key={value} className="flex items-start gap-3 cursor-pointer">
            <input
              type="radio"
              name="compress-quality"
              value={value}
              checked={quality === value}
              onChange={() => select(value)}
              className="mt-0.5"
            />
            <span>
              <span className="text-sm font-medium">{label}</span>
              <span className="block text-xs text-neutral-500">{description}</span>
            </span>
          </label>
        ))}
      </div>
    </fieldset>
  );
}
