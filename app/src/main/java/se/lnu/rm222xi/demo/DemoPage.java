package se.lnu.rm222xi.demo;

import java.util.ArrayList;
import java.util.List;
import se.lnu.rm222xi.svgcharts.Chart;

/**
 * An HTML page that shows one chart per visual test case, each with a description of what the
 * tester should see.
 */
class DemoPage {

  private final List<String> sections = new ArrayList<>();

  /**
   * Adds a test case to the page.
   *
   * @param heading a short name for the test case
   * @param expected what the tester should see in the chart
   * @param chart the chart to draw
   */
  void addTestCase(String heading, String expected, Chart chart) {
    sections.add("<section>\n"
        + "<h2>" + escapeHtml(heading) + "</h2>\n"
        + "<p><strong>Förväntat:</strong> " + escapeHtml(expected) + "</p>\n"
        + chart.toSvg() + "\n"
        + "</section>\n");
  }

  /**
   * Returns the complete HTML document.
   *
   * @return the page as HTML
   */
  String toHtml() {
    return "<!DOCTYPE html>\n"
        + "<html lang=\"sv\">\n"
        + "<head>\n"
        + "<meta charset=\"utf-8\">\n"
        + "<title>svg-charts – visuella testfall</title>\n"
        + "<style>body { font-family: sans-serif; margin: 2rem; } "
        + "section { margin-bottom: 3rem; } svg { border: 1px solid #ccc; }</style>\n"
        + "</head>\n"
        + "<body>\n"
        + "<h1>svg-charts – visuella testfall</h1>\n"
        + String.join("", sections)
        + "</body>\n"
        + "</html>\n";
  }

  private String escapeHtml(String text) {
    return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
