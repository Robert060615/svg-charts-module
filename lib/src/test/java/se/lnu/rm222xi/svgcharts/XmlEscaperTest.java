package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("XmlEscaper")
class XmlEscaperTest {

  private final XmlEscaper escaper = new XmlEscaper();

  @Test
  @DisplayName("should replace all five special characters with entities")
  void escapesAllSpecialCharacters() {
    assertEquals("&amp;&lt;&gt;&quot;&apos;", escaper.escape("&<>\"'"));
  }

  @Test
  @DisplayName("should leave ordinary text, including åäö, untouched")
  void leavesOrdinaryTextUntouched() {
    assertEquals("Temperatur i Malmö", escaper.escape("Temperatur i Malmö"));
  }

  @Test
  @DisplayName("should escape special characters mixed with text")
  void escapesMixedText() {
    assertEquals("Salt &amp; peppar &lt;3", escaper.escape("Salt & peppar <3"));
  }

  @Test
  @DisplayName("should return an empty string for empty input")
  void handlesEmptyString() {
    assertEquals("", escaper.escape(""));
  }
}
