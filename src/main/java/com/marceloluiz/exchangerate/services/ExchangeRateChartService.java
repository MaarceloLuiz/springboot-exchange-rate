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
                    .width(1000)
                    .height(600)
                    .title("1 " + baseCurrency.toUpperCase() + " = " + String.format("%.5f", latestRate) + " " + targetCurrency.toUpperCase())
                    .xAxisTitle("")
                    .yAxisTitle("Exchange Rate")
                    .build();

            // Customize the chart style
            chart.getStyler().setLegendVisible(false);
            chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 24));
            chart.getStyler().setAxisTitlesVisible(false);
            chart.getStyler().setYAxisDecimalPattern("#,##0.00000"); // Format as currency
            chart.getStyler().setChartBackgroundColor(Color.WHITE); // Plain white background
            chart.getStyler().setPlotBackgroundColor(Color.WHITE); // Plain white plot background
            chart.getStyler().setPlotBorderVisible(false); // Remove plot border
            chart.getStyler().setPlotGridVerticalLinesVisible(false); // Remove vertical gridlines
            chart.getStyler().setPlotGridHorizontalLinesVisible(false); // Remove horizontal gridlines
            chart.getStyler().setXAxisTicksVisible(false); // Hide X-axis tick marks
            chart.getStyler().setYAxisTicksVisible(false); // Hide Y-axis tick marks

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
