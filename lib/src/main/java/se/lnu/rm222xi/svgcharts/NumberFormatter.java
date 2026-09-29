package se.lnu.rm222xi.svgcharts;

import java.util.Locale;

/**
 * Writes numbers the way SVG expects them: rounded to at most two decimals, without trailing zeros,
 * and always with a dot as the decimal separator, whatever the computer's language settings are.
 */
class NumberFormatter {

  /**
   * Formats a number for use in SVG, for example 100.0 as "100" and 12.3456 as "12.35".
   *
   * @param value the number to format
   * @return the number as text
   */
  String format(double value) {
    String formatted = String.format(Locale.ROOT, "%.2f", value);
    while (formatted.endsWith("0")) {
      formatted = formatted.substring(0, formatted.length() - 1);
    }
    if (formatted.endsWith(".")) {
      formatted = formatted.substring(0, formatted.length() - 1);
    }
    return "-0".equals(formatted) ? "0" : formatted;
  }
}
