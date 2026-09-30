# Test Report

## Summary

The module is tested in two ways:

1. **Automated unit tests** with JUnit 5, in `lib/src/test/java`. They check the numbers and text in
   the generated SVG: axis steps, pixel positions, element counts, escaping, number formatting and
   error messages. The tests check properties of the output, such as "contains 5 `<rect>`" or
   "the zero line is at y 200", rather than comparing whole SVG strings, so they do not break when
   unrelated details change. One test class, `publicapi/PublicApiTest`, lies in its own package and
   can therefore only use the public interface, exactly like a programmer who has installed the
   library.
2. **Manual visual tests** with the test app in `app/`. Unit tests cannot tell whether a chart looks
   right, so the test app draws 13 charts (VT1–VT13) on one HTML page, each with a description of
   what should be seen. The charts were checked by eye in a web browser.

I chose this combination because the core of the module is calculation (readable axis steps,
scaling values to pixels), which unit tests check precisely, while the result is a picture, which
only a person can judge.

### How to repeat the tests

Requires JDK 25.

```bash
./gradlew check        # unit tests + Checkstyle + PMD
./gradlew :app:run     # writes app/demo.html
```

`./gradlew check` prints every test and writes a summary to `lib/build/reports/test-summary.log`.
For the visual tests, open `app/demo.html` in a web browser and compare each chart with the text
under its heading.

### Environment

| | |
|---|---|
| Date | 2026-09-30 |
| Operating system | macOS 14.4 |
| JDK | Temurin 25 (through the Gradle toolchain) |
| Browser for visual tests | Firefox |

### Result

- **Unit tests: 105 of 105 passed.**
- **Visual tests: 10 of 13 passed.** VT10, VT12 and VT13 show known limitations that are
  described in the [README](README.md#known-limitations).
- Checkstyle and PMD: 0 errors. PMD gives mild warnings (priority 4–5) that fields lack comments.
  I have not added them, since the field names already say what the fields are.

## Test Results

### Unit tests

| What was tested | How it was tested | Result |
| --- | --- | --- |
| `TickGenerator` chooses readable steps: 1, 2 or 5 times a power of ten | `TickGeneratorTest`, 7 tests: e.g. 0–87 gives step 20, 0–1 gives 0.2, 0–320 gives 100, 0–1 000 000 gives 200 000 | ✅ Passed |
| `TickGenerator` gives an axis that covers all values, from a tick at or below min to a tick at or above max | `TickGeneratorTest`: 0–87 gives 0, 20 … 100; 3–18 gives 0–20; −50–50 gives −60–60; 0–40 gets no extra ticks | ✅ Passed |
| `TickGenerator` gives exact decimals, without floating point noise such as 0.6000000000000001 | `TickGeneratorTest`: 0–1 gives exactly 0.0, 0.2 … 1.0; 0.6–1.4 gets no extra tick at 0.4 | ✅ Passed |
| `TickGenerator` edge cases | `TickGeneratorTest`: only negatives (−87 to −3 gives −100–0), min = max positive and negative, only zeros (0–1), min > max throws, fewer than 2 ticks throws | ✅ Passed |
| `LinearScale.toPixel` converts values to pixels | `LinearScaleTest`, 9 tests: min, max and middle; inverted y-axis; negative domain; values outside the domain; min = max gives the middle without dividing by zero | ✅ Passed |
| `XmlEscaper` makes text safe in SVG | `XmlEscaperTest`: all of `& < > " '` become entities; text with åäö is untouched; mixed text; empty string | ✅ Passed |
| `SvgElement` writes correct XML | `SvgElementTest`, 8 tests: self-closing tag, attribute order, replacing an attribute, text, nested children, escaping | ✅ Passed |
| `NumberFormatter` writes numbers the way SVG expects | `NumberFormatterTest`, 6 tests: 100.0 as `100`, 12.3456 as `12.35`, 0.5 as `0.5`, −0.001 as `0`, minus sign kept, decimal dot with Swedish locale | ✅ Passed |
| `ChartOptions` defaults and validation | `ChartOptionsTest`, 12 tests: defaults 600 × 400 and `#1f77b4`; 100 accepted, 99 and −5 throw with exact message; old value kept after an error; `#f80` and `#ff8800` accepted; `ff8800`, `#ff88`, `#gg0000`, `red` and `null` throw | ✅ Passed |
| `ChartDataException` | `ChartDataExceptionTest`: keeps its message and is an `IllegalArgumentException` | ✅ Passed |
| `setTitle`, `setLabels`, `setValues` store and validate input | `ChartTest`: values are stored; `null` lists, `null` items (with index in the message), infinity and NaN throw; old values are kept after an error; changing the caller's list afterwards does not change the chart | ✅ Passed |
| `toSvg()` builds a complete SVG document | `ChartTest`: `<svg>` with `xmlns`, size and `viewBox`; title both as `<title>` and visible text; escaped title; works without labels | ✅ Passed |
| `toSvg()` validates before drawing | `ChartTest`: no values gives `Cannot render chart without values. Call setValues() first.`; 5 labels and 4 values gives `Expected 5 values to match labels, got 4` | ✅ Passed |
| Axes: one grid line and one number per tick, labels under the plot area | `ChartTest`, 5 tests: 0–87 gives 6 lines and the numbers 0 and 100; 0.6 written without noise; 1 000 000 written as `1000000`, not `1E+6`; lowest tick at the bottom; labels escaped | ✅ Passed |
| `BarChart` draws one bar per value from the zero line | `BarChartTest`, 8 tests: 5 values give 5 bars; exact position and height of a positive and a negative bar; zero gives a bar without height; y-axis includes 0 for only positive and only negative values; label under the middle of its bar; color from options | ✅ Passed |
| `LineChart` draws a line with a dot per value | `LineChartTest`, 7 tests: exact path `M 50 360 L 315 200 L 580 40`; one `<circle>` per value; a single value in the middle; y-axis does not have to start at 0; equal values give a straight line; labels under the points; color on line and dots | ✅ Passed |
| Every chart type, through the public interface only | `PublicApiTest`, 6 tests on both `BarChart` and `LineChart`: starts with `<svg`; contains `<title>`; `toSvg()` twice gives the same result; no decimal commas with Swedish locale; invalid input can be caught as `ChartDataException` and as `IllegalArgumentException` | ✅ Passed |
| The locale test really catches the error it is meant to catch | Manual check: temporarily removed `Locale.ROOT` from `NumberFormatter` and ran `PublicApiTest` | ✅ The test failed as it should, and passed again when the code was restored |

### Visual tests

Each row refers to a chart in `app/demo.html`, created by `app/src/main/java/se/lnu/rm222xi/demo/VisualTestCases.java`.

| What was tested | How it was tested | Result |
| --- | --- | --- |
| VT1 – Bar chart | Five values 12–18 with labels Mån–Fre. Expected: five blue bars, labels centered under them, y-axis 0–20 in steps of 5 | ✅ Passed |
| VT2 – Negative bars | Values 4, −3, −8.5, 2, −1. Expected: Tis, Ons and Fre go down from the zero line, y-axis −10–4 in steps of 2 | ✅ Passed |
| VT3 – Only negative bars | Values −12, −15, −4. Expected: all bars hang down from 0, which is at the top of the y-axis | ✅ Passed |
| VT4 – Own color and size | `ChartOptions` with 400 × 250 and `#e4572e`. Expected: orange-red bars in a smaller chart | ✅ Passed |
| VT5 – Line chart | Six values 98–115.5. Expected: blue line with one dot per month, y-axis 95–120 that does not start at 0 | ✅ Passed |
| VT6 – Line with equal values | Four values of 40. Expected: a horizontal line at the top, y-axis 0–40, no crash | ✅ Passed |
| VT7 – Line with one value | One value, 7. Expected: a single dot in the middle with the label under it | ✅ Passed |
| VT8 – Small decimals | Values 0.12–0.61. Expected: y-axis 0.1, 0.2 … 0.7 with a decimal dot and no long decimals | ✅ Passed |
| VT9 – Special characters | Title `Åäö & <tecken> "citat"` and labels Göteborg, Malmö, Örebro. Expected: all characters shown exactly as written | ✅ Passed |
| VT10 – Minimum size 100 × 100 | Smallest allowed size. Expected: chart fits inside its frame and nothing crashes | ⚠️ Partly passed: no crash and the chart fits, but the numbers on the y-axis overlap because the plot area is only 20 pixels high |
| VT11 – Many values without labels | 40 values in a wave shape, no labels. Expected: 40 narrow bars above and below zero | ✅ Passed |
| VT12 – Large numbers | Values 1 200 000–2 380 000. Expected: numbers written in full, not as `1E+6` | ⚠️ Partly passed: numbers are written in full, but the longest (`2400000`) is cut off at the left edge |
| VT13 – Long labels | Five labels of up to 24 characters. Expected: shows the known limitation | ⚠️ Known limitation: labels are not shortened and run into each other |
