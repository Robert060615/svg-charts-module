package se.lnu.rm222xi.svgcharts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A node in an SVG document: a tag name, attributes in insertion order, and text or child
 * elements. All attribute values and text are escaped when the element is written as XML.
 */
class SvgElement {

  private final String tagName;
  private final Map<String, String> attributes = new LinkedHashMap<>();
  private final List<SvgElement> children = new ArrayList<>();
  private final XmlEscaper escaper = new XmlEscaper();
  private final NumberFormatter numberFormatter = new NumberFormatter();
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
   * @param name the attribute name, for example {@code "fill"}
   * @param value the attribute value, escaped when the element is written
   * @return this element, so calls can be chained
   */
  SvgElement setAttribute(String name, String value) {
    attributes.put(name, value);
    return this;
  }

  /**
   * Sets a numeric attribute, rounded to at most two decimals and always written with a dot as the
   * decimal separator, whatever the computer's language settings are.
   *
   * @param name the attribute name, for example {@code "x"}
   * @param value the number to write
   * @return this element, so calls can be chained
   */
  SvgElement setAttribute(String name, double value) {
    return setAttribute(name, numberFormatter.format(value));
  }

  /**
   * Sets the text between the start and end tag.
   *
   * @param text the text, escaped when the element is written
   * @return this element, so calls can be chained
   */
  SvgElement setText(String text) {
    this.text = text;
    return this;
  }

  /**
   * Places another element inside this one, after any children added earlier.
   *
   * @param child the element to place inside
   * @return this element, so calls can be chained
   */
  SvgElement addChild(SvgElement child) {
    children.add(child);
    return this;
  }

  /**
   * Writes the element and everything inside it as XML.
   *
   * @return the XML markup
   */
  @Override
  public String toString() {
    StringBuilder xml = new StringBuilder();
    appendTo(xml);
    return xml.toString();
  }

  private void appendTo(StringBuilder xml) {
    xml.append('<').append(tagName);
    appendAttributes(xml);

    if (hasNoContent()) {
      xml.append("/>");
    } else {
      xml.append('>');
      appendContent(xml);
      xml.append("</").append(tagName).append('>');
    }
  }

  private void appendAttributes(StringBuilder xml) {
    for (Map.Entry<String, String> attribute : attributes.entrySet()) {
      xml.append(' ')
          .append(attribute.getKey())
          .append("=\"")
          .append(escaper.escape(attribute.getValue()))
          .append('"');
    }
  }

  private boolean hasNoContent() {
    return text.isEmpty() && children.isEmpty();
  }

  private void appendContent(StringBuilder xml) {
    xml.append(escaper.escape(text));
    for (SvgElement child : children) {
      child.appendTo(xml);
    }
  }
}
