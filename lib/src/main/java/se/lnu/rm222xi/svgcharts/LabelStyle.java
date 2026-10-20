package se.lnu.rm222xi.svgcharts;

/**
 * The font settings shared by all small text in a chart: the numbers on the y-axis and the labels
 * under the x-axis.
 */
class LabelStyle {

  private static final int FONT_SIZE = 12;

  /**
   * Creates an empty group whose text children all get the label font. Setting the font once on
   * the group keeps the SVG smaller than setting it on every text element.
   *
   * @param textAnchor how the text is aligned to its x-coordinate: {@code "start"},
   *     {@code "middle"} or {@code "end"}
   * @return the group, ready for text elements to be added
   */
  SvgElement createGroup(String textAnchor) {
    return new SvgElement("g")
        .setAttribute("font-family", Chart.FONT_FAMILY)
        .setAttribute("font-size", FONT_SIZE)
        .setAttribute("text-anchor", textAnchor);
  }
}
