package se.lnu.rm222xi.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    new VisualTestCases(page).addAll();

    Path file = Path.of("demo.html").toAbsolutePath();
    Files.writeString(file, page.toHtml());
    System.out.println("Wrote " + file);
    System.out.println("Open it in a web browser to check the charts.");
  }
}
