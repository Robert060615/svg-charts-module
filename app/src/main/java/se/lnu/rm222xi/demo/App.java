package se.lnu.rm222xi.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import se.lnu.rm222xi.svgcharts.BarChart;
import se.lnu.rm222xi.svgcharts.LineChart;

/**
 * Test app for the svg-charts library. Writes demo.html with one chart per visual test case, to be
 * opened in a web browser and checked by eye.
 */
public class App {

  /**
   * Execution entry point.
   *
   * @param args command-line arguments (unused)
   * @throws IOException if demo.html cannot be written
   */
  public static void main(String[] args) throws IOException {
    DemoPage page = new DemoPage();

    BarChart barChart = new BarChart();
    barChart.setTitle("Temperatur i Lund");
    barChart.setLabels(List.of("Mån", "Tis", "Ons", "Tor", "Fre"));
    barChart.setValues(List.of(14.0, 16.0, 12.0, 18.0, 15.0));
    page.addTestCase("Stapeldiagram", "Fem blå staplar, y-axel 0–20 i steg om 5.", barChart);

    LineChart lineChart = new LineChart();
    lineChart.setTitle("Aktiekurs");
    lineChart.setLabels(List.of("Jan", "Feb", "Mar", "Apr", "Maj", "Jun"));
    lineChart.setValues(List.of(102.5, 98.0, 110.25, 107.0, 115.5, 112.0));
    page.addTestCase("Linjediagram",
        "En blå linje med sex punkter, y-axel 95–120 som inte börjar på 0.", lineChart);

    Path file = Path.of("demo.html").toAbsolutePath();
    Files.writeString(file, page.toHtml());
    System.out.println("Wrote " + file);
    System.out.println("Open it in a web browser to check the charts.");
  }
}
