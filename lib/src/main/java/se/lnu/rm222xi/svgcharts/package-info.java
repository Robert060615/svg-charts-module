/**
 * Draws bar charts and line charts as SVG text, with readable steps on the y-axis chosen
 * automatically.
 *
 * <p>Create a {@link BarChart} or a {@link LineChart}, set its values and, if you like, a title
 * and one label per value, then call {@code toSvg()}. Size and color are set with
 * {@link ChartOptions}. Invalid input is reported right away with a {@link ChartDataException}
 * whose message tells what is wrong. Everything you can do with a chart is listed in
 * {@link SvgChart}.
 *
 * <pre>{@code
 * ChartOptions options = new ChartOptions();
 * options.setWidth(800);
 * options.setColor("#e4572e");
 *
 * BarChart chart = new BarChart(options);
 * chart.setTitle("Temperatur i Lund");
 * chart.setLabels(List.of("Mån", "Tis"));
 * chart.setValues(List.of(3.0, -1.0));
 * String svg = chart.toSvg();
 * }</pre>
 */
package se.lnu.rm222xi.svgcharts;
