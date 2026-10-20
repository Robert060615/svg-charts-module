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
    String rounded = String.format(Locale.ROOT, "%.2f", value);
    String withoutTrailingZeros = removeTrailingZeros(rounded);
    return removeSignFromNegativeZero(withoutTrailingZeros);
  }

  private String removeTrailingZeros(String decimalNumber) {
    String trimmed = decimalNumber;
    while (trimmed.endsWith("0")) {
      trimmed = trimmed.substring(0, trimmed.length() - 1);
    }
    if (trimmed.endsWith(".")) {
      trimmed = trimmed.substring(0, trimmed.length() - 1);
    }
    return trimmed;
  }

  private String removeSignFromNegativeZero(String number) {
    return "-0".equals(number) ? "0" : number;
  }
}
