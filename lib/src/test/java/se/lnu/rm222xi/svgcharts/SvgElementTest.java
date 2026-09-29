package se.lnu.rm222xi.svgcharts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
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
}
