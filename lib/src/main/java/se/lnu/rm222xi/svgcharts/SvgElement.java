package se.lnu.rm222xi.svgcharts;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A node in an SVG document: a tag name, attributes in insertion order and optional text. All
 * attribute values and text are escaped when the element is written as XML.
 */
class SvgElement {

  private final String tagName;
  private final Map<String, String> attributes = new LinkedHashMap<>();
  private final XmlEscaper escaper = new XmlEscaper();
  private String text = "";

  /**
   * Creates an element without attributes or text.
   *
   * @param tagName the SVG tag, for example {@code "rect"}
   */
  SvgElement(String tagName) {
    this.tagName = tagName;
  }

  /**
   * Sets an attribute, replacing any earlier value with the same name.
   *
   * @return this element, so calls can be chained
   */
  SvgElement setAttribute(String name, String value) {
    attributes.put(name, value);
    return this;
  }

  /**
   * Sets the text between the start and end tag.
   *
   * @return this element, so calls can be chained
   */
  SvgElement setText(String text) {
    this.text = text;
    return this;
  }

  /**
   * Writes the element as XML.
   *
   * @return the XML markup
   */
  @Override
  public String toString() {
    StringBuilder xml = new StringBuilder();
    xml.append('<').append(tagName);
    for (Map.Entry<String, String> attribute : attributes.entrySet()) {
      xml.append(' ')
          .append(attribute.getKey())
          .append("=\"")
          .append(escaper.escape(attribute.getValue()))
          .append('"');
    }

    if (text.isEmpty()) {
      return xml.append("/>").toString();
    }

    return xml.append('>')
        .append(escaper.escape(text))
        .append("</").append(tagName).append('>')
        .toString();
  }
}
