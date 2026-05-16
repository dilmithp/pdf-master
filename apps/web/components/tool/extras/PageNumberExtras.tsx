"use client";

import { useState } from "react";

export type PageNumberFormat = "x" | "x-of-y";
export type PageNumberPosition =
  | "top-left"
  | "top-center"
  | "top-right"
  | "bottom-left"
  | "bottom-center"
  | "bottom-right";

export interface PageNumberExtrasValue {
  format: PageNumberFormat;
  position: PageNumberPosition;
  startPage: number;
}

interface PageNumberExtrasProps {
  onChange: (value: PageNumberExtrasValue) => void;
}

const POSITIONS: { value: PageNumberPosition; label: string }[] = [
  { value: "top-left", label: "Top left" },
  { value: "top-center", label: "Top center" },
  { value: "top-right", label: "Top right" },
  { value: "bottom-left", label: "Bottom left" },
  { value: "bottom-center", label: "Bottom center" },
  { value: "bottom-right", label: "Bottom right" },
];

export function PageNumberExtras({ onChange }: PageNumberExtrasProps) {
  const [format, setFormat] = useState<PageNumberFormat>("x");
  const [position, setPosition] = useState<PageNumberPosition>("bottom-center");
  const [startPage, setStartPage] = useState(1);

  function emit(updates: Partial<PageNumberExtrasValue>) {
    onChange({ format, position, startPage, ...updates });
  }

  return (
    <fieldset className="space-y-4 rounded-lg border border-neutral-200 p-4 dark:border-neutral-700">
      <legend className="px-1 text-sm font-medium">Page number options</legend>

      <div>
        <p className="text-xs text-neutral-600 dark:text-neutral-400 mb-2">Format</p>
        <div className="flex gap-4">
          {([["x", "1, 2, 3 …"], ["x-of-y", "1 of 10, 2 of 10 …"]] as [PageNumberFormat, string][]).map(
            ([value, label]) => (
              <label key={value} className="flex items-center gap-2 cursor-pointer text-sm">
                <input
                  type="radio"
                  name="pn-format"
                  value={value}
                  checked={format === value}
                  onChange={() => {
                    setFormat(value);
                    emit({ format: value });
                  }}
                />
                {label}
              </label>
            ),
          )}
        </div>
      </div>

      <div>
        <label htmlFor="pn-position" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
          Position
        </label>
        <select
          id="pn-position"
          value={position}
          onChange={(e) => {
            const p = e.target.value as PageNumberPosition;
            setPosition(p);
            emit({ position: p });
          }}
          className="w-full rounded-md border border-neutral-300 px-3 py-1.5 text-sm dark:border-neutral-600 dark:bg-neutral-800"
        >
          {POSITIONS.map(({ value, label }) => (
            <option key={value} value={value}>{label}</option>
          ))}
        </select>
      </div>

      <div>
        <label htmlFor="pn-start" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
          Start numbering from page
        </label>
        <input
          id="pn-start"
          type="number"
          min={1}
          value={startPage}
          onChange={(e) => {
            const n = Math.max(1, Number(e.target.value));
            setStartPage(n);
            emit({ startPage: n });
          }}
          className="w-24 rounded-md border border-neutral-300 px-3 py-1.5 text-sm dark:border-neutral-600 dark:bg-neutral-800"
        />
      </div>
    </fieldset>
  );
}
