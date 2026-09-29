package se.lnu.rm222xi.svgcharts;

/**
 * Thrown when a chart is given invalid data or options, for example a negative width, a malformed
 * color, or a different number of values than labels. The message tells exactly what is wrong.
 */
public class ChartDataException extends IllegalArgumentException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates an exception with a message that describes the problem.
   *
   * @param message what is wrong and, if possible, how to fix it
   */
  ChartDataException(String message) {
    super(message);
  }
}
