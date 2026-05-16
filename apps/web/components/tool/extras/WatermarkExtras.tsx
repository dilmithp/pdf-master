"use client";

import { useState } from "react";

export type WatermarkPosition =
  | "top-left"
  | "top-center"
  | "top-right"
  | "center"
  | "bottom-left"
  | "bottom-center"
  | "bottom-right";

export interface WatermarkExtrasValue {
  text: string;
  fontSize: number;
  opacity: number;
  position: WatermarkPosition;
}

interface WatermarkExtrasProps {
  onChange: (value: WatermarkExtrasValue) => void;
}

const POSITIONS: { value: WatermarkPosition; label: string }[] = [
  { value: "top-left", label: "Top left" },
  { value: "top-center", label: "Top center" },
  { value: "top-right", label: "Top right" },
  { value: "center", label: "Center" },
  { value: "bottom-left", label: "Bottom left" },
  { value: "bottom-center", label: "Bottom center" },
  { value: "bottom-right", label: "Bottom right" },
];

export function WatermarkExtras({ onChange }: WatermarkExtrasProps) {
  const [text, setText] = useState("CONFIDENTIAL");
  const [fontSize, setFontSize] = useState(48);
  const [opacity, setOpacity] = useState(30);
  const [position, setPosition] = useState<WatermarkPosition>("center");

  function emit(updates: Partial<WatermarkExtrasValue>) {
    onChange({ text, fontSize, opacity, position, ...updates });
  }

  return (
    <fieldset className="space-y-4 rounded-lg border border-neutral-200 p-4 dark:border-neutral-700">
      <legend className="px-1 text-sm font-medium">Watermark options</legend>

      <div>
        <label htmlFor="wm-text" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
          Watermark text
        </label>
        <input
          id="wm-text"
          type="text"
          value={text}
          onChange={(e) => {
            setText(e.target.value);
            emit({ text: e.target.value });
          }}
          placeholder="e.g. CONFIDENTIAL"
          className="w-full rounded-md border border-neutral-300 px-3 py-1.5 text-sm dark:border-neutral-600 dark:bg-neutral-800"
        />
      </div>

      <div className="flex gap-4">
        <div className="flex-1">
          <label htmlFor="wm-font-size" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
            Font size (pt)
          </label>
          <input
            id="wm-font-size"
            type="number"
            min={8}
            max={200}
            value={fontSize}
            onChange={(e) => {
              const n = Math.min(200, Math.max(8, Number(e.target.value)));
              setFontSize(n);
              emit({ fontSize: n });
            }}
            className="w-full rounded-md border border-neutral-300 px-3 py-1.5 text-sm dark:border-neutral-600 dark:bg-neutral-800"
          />
        </div>

        <div className="flex-1">
          <label htmlFor="wm-opacity" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
            Opacity ({opacity}%)
          </label>
          <input
            id="wm-opacity"
            type="range"
            min={5}
            max={100}
            step={5}
            value={opacity}
            onChange={(e) => {
              const n = Number(e.target.value);
              setOpacity(n);
              emit({ opacity: n });
            }}
            className="w-full"
          />
        </div>
      </div>

      <div>
        <label htmlFor="wm-position" className="block text-xs text-neutral-600 dark:text-neutral-400 mb-1">
          Position
        </label>
        <select
          id="wm-position"
          value={position}
          onChange={(e) => {
            const p = e.target.value as WatermarkPosition;
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
    </fieldset>
  );
}
