package com.marceloluiz.exchangerate.services;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeRateChartService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void generateChart(List<String> rawDates, List<Double> rates, String baseCurrency, String targetCurrency) {
        try {
            List<Date> dates = convertDates(rawDates);

            // Get the most recent exchange rate
            double latestRate = rates.getLast();

            // Create the chart
            XYChart chart = new XYChartBuilder()
                    .width(800)
                    .height(400)
                    .title("1 " + baseCurrency.toUpperCase() + " = " + String.format("%.5f", latestRate) + " " + targetCurrency.toUpperCase())
                    .xAxisTitle("")
                    .yAxisTitle("")
                    .build();

            // Customize the chart style
            chart.getStyler().setLegendVisible(false);
            chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 24));
            chart.getStyler().setAxisTitlesVisible(true);
            chart.getStyler().setYAxisDecimalPattern("#,##0.0000"); // Format BRL rates
            chart.getStyler().setChartBackgroundColor(Color.WHITE); // Plain white background
            chart.getStyler().setPlotBackgroundColor(Color.WHITE); // Plain white plot background
            chart.getStyler().setPlotBorderVisible(false); // Remove plot border
            chart.getStyler().setPlotGridVerticalLinesVisible(false); // Remove vertical gridlines
            chart.getStyler().setPlotGridHorizontalLinesVisible(false); // Remove horizontal gridlines
            chart.getStyler().setYAxisTicksVisible(true); // Show Y-axis ticks
            chart.getStyler().setXAxisTicksVisible(true); // Show X-axis ticks
            chart.getStyler().setXAxisTickMarkSpacingHint(80); // Control X-axis tick spacing
            chart.getStyler().setPlotGridLinesVisible(false); // Remove all gridlines


            // Customize X-axis labels to show only key dates
            chart.setCustomXAxisTickLabelsFormatter(date -> {
                return new SimpleDateFormat("dd-MM").format(date); // Format and display all dates
            });

            // Add a thin dotted horizontal line for the latest rate
            XYSeries horizontalLine = chart.addSeries("Horizontal Line",
                    List.of(dates.getFirst(), dates.getLast()),
                    List.of(latestRate, latestRate));
            horizontalLine.setMarker(SeriesMarkers.NONE); // No markers
            horizontalLine.setLineColor(new Color(50, 50, 255)); // Blue color
            horizontalLine.setLineStyle(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{5.0f}, 0.0f)); // Thin dotted line

            // Add the main data series
            XYSeries series = chart.addSeries(baseCurrency.toUpperCase() + " to " + targetCurrency.toUpperCase(), dates, rates);
            series.setMarker(SeriesMarkers.NONE); // No markers for data points
            series.setLineColor(Color.BLUE); // Normal blue line
            series.setLineStyle(new BasicStroke(2.0f)); // Solid line

            createChartsDirectory();
            BitmapEncoder.saveBitmap(chart, "charts/chart", BitmapEncoder.BitmapFormat.PNG);

            System.out.println("Chart saved successfully!");
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate chart", e);
        }
    }

    private void createChartsDirectory(){
        File chartsDirectory = new File("charts");
        if (!chartsDirectory.exists()) {
            if (chartsDirectory.mkdir()) {
                System.out.println("Directory 'charts' created successfully.");
            } else {
                System.err.println("Failed to create 'charts' directory.");
            }
        } else {
            System.out.println("Directory 'charts' already exists.");
        }
    }

    private List<Date> convertDates(List<String> stringDates) {
        return stringDates.stream()
                .map(dateString -> {
                    try {
                        return dateFormat.parse(dateString); // Convert String to Date
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid date format", e);
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }
}
