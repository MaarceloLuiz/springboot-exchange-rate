package com.marceloluiz.exchangerate.services;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ExchangeRateChartService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void generateChart(List<String> rawDates, List<Double> rates, String baseCurrency, String targetCurrency) {
        try {
            List<Date> dates = convertDates(rawDates);

            LinkedHashMap<Date, Double> uniqueData = new LinkedHashMap<>();
            for (int i = 0; i < dates.size(); i++) {
                uniqueData.put(dates.get(i), rates.get(i));
            }

            List<Date> uniqueDates = new ArrayList<>(uniqueData.keySet());
            List<Double> uniqueRates = new ArrayList<>(uniqueData.values());

            TimeSeries series = new TimeSeries(baseCurrency.toUpperCase() + " to " + targetCurrency.toUpperCase());
            for (int i = 0; i < uniqueDates.size(); i++) {
                series.add(new Day(uniqueDates.get(i)), uniqueRates.get(i));
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "1 " + baseCurrency.toUpperCase() + " = " + String.format("%.4f", rates.getLast()) + " " + targetCurrency.toUpperCase(),
                    "",
                    "",
                    dataset,
                    false,
                    false,
                    false
            );

            // Update title font
            chart.setTitle(new org.jfree.chart.title.TextTitle(
                    "1 " + baseCurrency.toUpperCase() + " = " + String.format("%.4f", rates.get(rates.size() - 1)) + " " + targetCurrency.toUpperCase(),
                    new Font("Arial", Font.BOLD, 24)
            ));

            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));

            TimeSeries horizontalLineSeries = new TimeSeries("Latest Rate");
            double latestRate = uniqueRates.getLast();
            horizontalLineSeries.add(new Day(uniqueDates.getFirst()), latestRate);
            horizontalLineSeries.add(new Day(uniqueDates.getLast()), latestRate);
            dataset.addSeries(horizontalLineSeries);

            renderer.setSeriesPaint(1, new Color(50, 50, 255));
            renderer.setSeriesStroke(1, new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1.0f, new float[]{5.0f}, 0.0f));
            plot.setRenderer(renderer);

            DateAxis xAxis = (DateAxis) plot.getDomainAxis();
            xAxis.setDateFormatOverride(new SimpleDateFormat("d MMM yy"));
            xAxis.setRange(uniqueDates.getFirst(), uniqueDates.getLast());

            NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
            yAxis.setNumberFormatOverride(new java.text.DecimalFormat("#,##0.00 " + targetCurrency.toUpperCase()));

            createChartsDirectory();
            File outputFile = new File("charts/chart.png");
            ImageIO.write(chart.createBufferedImage(800, 400), "png", outputFile);

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
