package se.lnu.rm222xi.svgcharts;

/**
 * Replaces the characters that have special meaning in XML with their entities.
 */
class XmlEscaper {

  /**
   * Escapes {@code & < > " '} so the text is safe inside both element content and attribute
   * values.
   *
   * @param text the raw text
   * @return the escaped text
   */
  String escape(String text) {
    StringBuilder escaped = new StringBuilder(text.length());
    for (char character : text.toCharArray()) {
      escaped.append(toEntity(character));
    }
    return escaped.toString();
  }

  private String toEntity(char character) {
    return switch (character) {
      case '&' -> "&amp;";
      case '<' -> "&lt;";
      case '>' -> "&gt;";
      case '"' -> "&quot;";
      case '\'' -> "&apos;";
      default -> String.valueOf(character);
    };
  }
}
