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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeRateChartService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void generateChart(List<String> rawDates, List<Double> rates, String baseCurrency, String targetCurrency) {
        try {
            List<Date> dates = convertDates(rawDates);
            double latestRate = rates.getLast();

            XYChart chart = new XYChartBuilder()
                    .width(800)
                    .height(400)
                    .title("1 " + baseCurrency.toUpperCase() + " = " + String.format("%.4f", latestRate) + " " + targetCurrency.toUpperCase())
                    .xAxisTitle("")
                    .yAxisTitle("")
                    .build();

            chart.getStyler().setLegendVisible(false);
            chart.getStyler().setChartTitleFont(new Font("Arial", Font.BOLD, 24));
            chart.getStyler().setAxisTitlesVisible(true);
            chart.getStyler().setYAxisDecimalPattern("#,##0.00 " + targetCurrency.toUpperCase());
            chart.getStyler().setChartBackgroundColor(Color.WHITE);
            chart.getStyler().setPlotBackgroundColor(Color.WHITE);
            chart.getStyler().setPlotBorderVisible(false);
            chart.getStyler().setPlotGridVerticalLinesVisible(false);
            chart.getStyler().setPlotGridHorizontalLinesVisible(false);
            chart.getStyler().setYAxisTicksVisible(true);
            chart.getStyler().setXAxisTicksVisible(true);
            chart.getStyler().setXAxisTickMarkSpacingHint(80);
            chart.getStyler().setPlotGridLinesVisible(false);

            chart.setCustomXAxisTickLabelsFormatter(date -> new SimpleDateFormat("d MMM").format(date));

            XYSeries horizontalLine = chart.addSeries("Horizontal Line",
                    List.of(dates.getFirst(), dates.getLast()),
                    List.of(latestRate, latestRate));
            horizontalLine.setMarker(SeriesMarkers.NONE);
            horizontalLine.setLineColor(new Color(50, 50, 255));
            horizontalLine.setLineStyle(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{5.0f}, 0.0f)); // Thin dotted line

            XYSeries series = chart.addSeries(baseCurrency.toUpperCase() + " to " + targetCurrency.toUpperCase(), dates, rates);
            series.setMarker(SeriesMarkers.NONE);
            series.setLineColor(Color.BLUE);
            series.setLineStyle(new BasicStroke(2.0f));

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
