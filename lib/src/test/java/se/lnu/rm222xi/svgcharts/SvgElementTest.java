package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SvgElement")
class SvgElementTest {

  @Test
  @DisplayName("should self-close an element without text")
  void selfClosesEmptyElement() {
    assertEquals("<rect/>", new SvgElement("rect").toString());
  }

  @Test
  @DisplayName("should write attributes in the order they were set")
  void keepsAttributeOrder() {
    SvgElement rect = new SvgElement("rect")
        .setAttribute("y", "2")
        .setAttribute("x", "1")
        .setAttribute("fill", "#ff0000");

    assertEquals("<rect y=\"2\" x=\"1\" fill=\"#ff0000\"/>", rect.toString());
  }

  @Test
  @DisplayName("should replace the value when the same attribute is set twice")
  void replacesAttributeValue() {
    SvgElement rect = new SvgElement("rect")
        .setAttribute("x", "1")
        .setAttribute("x", "5");

    assertEquals("<rect x=\"5\"/>", rect.toString());
  }

  @Test
  @DisplayName("should write text between start and end tag")
  void writesText() {
    assertEquals("<title>Hej</title>", new SvgElement("title").setText("Hej").toString());
  }

  @Test
  @DisplayName("should escape attribute values and text")
  void escapesValuesAndText() {
    SvgElement text = new SvgElement("text")
        .setAttribute("font-family", "\"Arial\" & co")
        .setText("<Hej>");

    assertEquals("<text font-family=\"&quot;Arial&quot; &amp; co\">&lt;Hej&gt;</text>",
        text.toString());
  }

  @Test
  @DisplayName("should write children inside the parent, in the order they were added")
  void writesChildren() {
    SvgElement group = new SvgElement("g")
        .addChild(new SvgElement("circle"))
        .addChild(new SvgElement("title").setText("Hej"));

    assertEquals("<g><circle/><title>Hej</title></g>", group.toString());
  }

  @Test
  @DisplayName("should write children of children")
  void writesNestedChildren() {
    SvgElement svg = new SvgElement("svg")
        .addChild(new SvgElement("g").addChild(new SvgElement("rect")));

    assertEquals("<svg><g><rect/></g></svg>", svg.toString());
  }

  @Nested
  @DisplayName("setAttribute(name, double)")
  class NumericAttributeTest {

    private final Locale originalLocale = Locale.getDefault();

    @AfterEach
    void restoreLocale() {
      Locale.setDefault(originalLocale);
    }

    @Test
    @DisplayName("should write whole numbers without decimals")
    void writesWholeNumbersWithoutDecimals() {
      assertEquals("<rect x=\"100\"/>", new SvgElement("rect").setAttribute("x", 100.0).toString());
    }

    @Test
    @DisplayName("should round to two decimals and remove trailing zeros")
    void roundsAndRemovesTrailingZeros() {
      SvgElement rect = new SvgElement("rect")
          .setAttribute("x", 12.3456)
          .setAttribute("y", 0.5);

      assertEquals("<rect x=\"12.35\" y=\"0.5\"/>", rect.toString());
    }

    @Test
    @DisplayName("should write zero and negative zero as 0")
    void writesZeroAsZero() {
      SvgElement rect = new SvgElement("rect")
          .setAttribute("x", 0.0)
          .setAttribute("y", -0.001);

      assertEquals("<rect x=\"0\" y=\"0\"/>", rect.toString());
    }

    @Test
    @DisplayName("should keep the minus sign on negative numbers")
    void keepsMinusSign() {
      assertEquals("<rect x=\"-2.5\"/>", new SvgElement("rect").setAttribute("x", -2.5).toString());
    }

    @Test
    @DisplayName("should use a dot as decimal separator even with Swedish settings")
    void usesDotWithSwedishLocale() {
      Locale.setDefault(Locale.of("sv", "SE"));

      assertEquals("<rect x=\"1.5\"/>", new SvgElement("rect").setAttribute("x", 1.5).toString());
    }
  }
}
