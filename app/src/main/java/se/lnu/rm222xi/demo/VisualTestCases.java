package se.lnu.rm222xi.demo;

import java.util.ArrayList;
import java.util.List;
import se.lnu.rm222xi.svgcharts.BarChart;
import se.lnu.rm222xi.svgcharts.Chart;
import se.lnu.rm222xi.svgcharts.ChartOptions;
import se.lnu.rm222xi.svgcharts.LineChart;

/**
 * The visual test cases, each with an id (VT1, VT2 …) that TEST_REPORT.md refers to.
 */
class VisualTestCases {

  private final DemoPage page;

  /**
   * Creates the test cases for the given page.
   *
   * @param page the page the test cases are added to
   */
  VisualTestCases(DemoPage page) {
    this.page = page;
  }

  /**
   * Adds every test case to the page, in order.
   */
  void addAll() {
    addBasicBarChart();
    addNegativeBars();
    addOnlyNegativeBars();
    addCustomColorAndSize();
    addBasicLineChart();
    addLineWithEqualValues();
    addLineWithSingleValue();
    addLineWithSmallDecimals();
    addSpecialCharacters();
    addMinimalSize();
    addManyValues();
    addLargeValues();
    addLongLabels();
  }

  private void addBasicBarChart() {
    Chart chart = withData(new BarChart(), "Temperatur i Lund",
        List.of("Mån", "Tis", "Ons", "Tor", "Fre"), List.of(14.0, 16.0, 12.0, 18.0, 15.0));
    page.addTestCase("VT1 – Stapeldiagram",
        "Fem blå staplar med etiketterna Mån–Fre centrerade under. Y-axel 0–20 i steg om 5.",
        chart);
  }

  private void addNegativeBars() {
    Chart chart = withData(new BarChart(), "Temperatur i Kiruna",
        List.of("Mån", "Tis", "Ons", "Tor", "Fre"), List.of(4.0, -3.0, -8.5, 2.0, -1.0));
    page.addTestCase("VT2 – Negativa staplar",
        "Tis, Ons och Fre går nedåt från nollinjen, Mån och Tor uppåt. Y-axel −10–4 i steg om 2.",
        chart);
  }

  private void addOnlyNegativeBars() {
    Chart chart = withData(new BarChart(), "Bara minusgrader",
        List.of("Jan", "Feb", "Mar"), List.of(-12.0, -15.0, -4.0));
    page.addTestCase("VT3 – Bara negativa staplar",
        "Alla staplar hänger nedåt från 0, som står överst på y-axeln.", chart);
  }

  private void addCustomColorAndSize() {
    ChartOptions options = new ChartOptions();
    options.setWidth(400);
    options.setHeight(250);
    options.setColor("#e4572e");
    Chart chart = withData(new BarChart(options), "Egen färg och storlek",
        List.of("A", "B", "C"), List.of(3.0, 7.0, 5.0));
    page.addTestCase("VT4 – Egen färg och storlek",
        "Orangeröda staplar i ett diagram som är 400 × 250 pixlar (mindre än de andra).", chart);
  }

  private void addBasicLineChart() {
    Chart chart = withData(new LineChart(), "Aktiekurs",
        List.of("Jan", "Feb", "Mar", "Apr", "Maj", "Jun"),
        List.of(102.5, 98.0, 110.25, 107.0, 115.5, 112.0));
    page.addTestCase("VT5 – Linjediagram",
        "En blå linje med en prick per månad. Y-axel 95–120 som inte börjar på 0.", chart);
  }

  private void addLineWithEqualValues() {
    Chart chart = withData(new LineChart(), "Alla värden lika",
        List.of("A", "B", "C", "D"), List.of(40.0, 40.0, 40.0, 40.0));
    page.addTestCase("VT6 – Linje med lika värden",
        "En vågrät linje överst vid 40. Y-axel 0–40. Ingen krasch och inget tomt diagram.",
        chart);
  }

  private void addLineWithSingleValue() {
    Chart chart = withData(new LineChart(), "Ett enda värde", List.of("Idag"), List.of(7.0));
    page.addTestCase("VT7 – Linje med ett värde",
        "En ensam prick mitt i diagrammet med etiketten Idag under.", chart);
  }

  private void addLineWithSmallDecimals() {
    Chart chart = withData(new LineChart(), "Små decimaltal",
        List.of("1", "2", "3", "4", "5"), List.of(0.12, 0.35, 0.28, 0.61, 0.54));
    page.addTestCase("VT8 – Små decimaltal",
        "Y-axeln visar 0.1, 0.2 … 0.7 med punkt och utan långa decimaler som 0.30000000000000004.",
        chart);
  }

  private void addSpecialCharacters() {
    Chart chart = withData(new BarChart(), "Åäö & <tecken> \"citat\"",
        List.of("Göteborg", "Malmö", "Örebro"), List.of(5.0, 8.0, 3.0));
    page.addTestCase("VT9 – Specialtecken",
        "Titeln visas exakt som Åäö & <tecken> \"citat\" och etiketterna med å, ä och ö.",
        chart);
  }

  private void addMinimalSize() {
    ChartOptions options = new ChartOptions();
    options.setWidth(100);
    options.setHeight(100);
    Chart chart = withData(new BarChart(options), "Liten",
        List.of("A", "B"), List.of(1.0, 2.0));
    page.addTestCase("VT10 – Minsta storlek 100 × 100",
        "Ett litet diagram inom ramen. Det får vara trångt, men inget ska krascha.", chart);
  }

  private void addManyValues() {
    List<Double> values = new ArrayList<>();
    for (int i = 0; i < 40; i++) {
      values.add(Math.round(Math.sin(i / 4.0) * 100) / 10.0);
    }
    Chart chart = withData(new BarChart(), "40 värden utan etiketter", List.of(), values);
    page.addTestCase("VT11 – Många värden utan etiketter",
        "40 smala staplar i en vågform, både över och under noll. Inga etiketter under.", chart);
  }

  private void addLargeValues() {
    Chart chart = withData(new LineChart(), "Invånare",
        List.of("1990", "2000", "2010", "2020"),
        List.of(1_200_000.0, 1_550_000.0, 2_100_000.0, 2_380_000.0));
    page.addTestCase("VT12 – Stora tal",
        "Y-axeln visar hela tal som 1000000 och inte 1E+6. Långa siffror kan gå utanför "
            + "vänsterkanten, vilket är en känd begränsning.",
        chart);
  }

  private void addLongLabels() {
    Chart chart = withData(new BarChart(), "Långa etiketter",
        List.of("Mycket lång etikett ett", "Mycket lång etikett två", "Kort",
            "Mycket lång etikett fyra", "Mycket lång etikett fem"),
        List.of(3.0, 5.0, 4.0, 2.0, 6.0));
    page.addTestCase("VT13 – Långa etiketter",
        "Känd begränsning: långa etiketter kortas inte av och kan gå in i varandra.", chart);
  }

  private Chart withData(Chart chart, String title, List<String> labels, List<Double> values) {
    chart.setTitle(title);
    chart.setLabels(labels);
    chart.setValues(values);
    return chart;
  }
}
