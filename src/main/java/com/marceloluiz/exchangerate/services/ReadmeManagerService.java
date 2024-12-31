package com.marceloluiz.exchangerate.services;

import com.marceloluiz.exchangerate.config.ExchangeRateProperties;
import com.marceloluiz.exchangerate.config.FileProperties;
import com.marceloluiz.exchangerate.model.ExchangeRate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ReadmeManagerService {
    private final ExchangeRateService exchangeRateService;
    private final MarkdownService markdownService;
    private final ExchangeRateChartService chartService;
    private final FileService fileService;
    private final FileProperties fileProperties;
    private final ExchangeRateProperties exchangeRateProperties;

    public void updateReadme(){
        try{
            String readme = fileService.readMarkdown(fileProperties.getREADME_PATH());
            List<ExchangeRate> exchangeRate = exchangeRateService.getExchangeRate();
            exchangeRate.sort(Comparator.comparing(ExchangeRate::getDate));

            List<String> dates = new ArrayList<>();
            List<Double> rates = new ArrayList<>();
            exchangeRate.forEach(item -> {
                dates.add(item.getDate());
                rates.add(item.getRates().values().iterator().next());
            });

            chartService.generateChart(dates, rates, exchangeRateProperties.getBaseCurrency(), exchangeRateProperties.getTargetCurrency());

            String newContent = markdownService.generateChartMarkdown(fileProperties.getCHART_IMAGE_PATH());
            readme = insertTable(readme, "<!-- EXCHANGE-RATE-START -->", "<!-- EXCHANGE-RATE-END -->", newContent);

            fileService.writeToMarkdown(readme, fileProperties.getREADME_PATH());

        }catch (IllegalStateException e){
            throw new RuntimeException("Required placeholders not found in README.", e);
        }catch (Exception e){
            throw new RuntimeException("Failed to update file", e);
        }
    }

    private String insertTable(String original, String startPlaceholder, String endPlaceholder, String content){
        if (!original.contains(startPlaceholder) || !original.contains(endPlaceholder)) {
            throw new IllegalStateException("Placeholders not found in README.md");
        }

        int startIndex = original.indexOf(startPlaceholder);
        int endIndex = original.indexOf(endPlaceholder);

        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            throw new IllegalStateException("Placeholders not found or invalid in the content.");
        }

        startIndex += startPlaceholder.length();
        String before = original.substring(0, startIndex);
        String after = original.substring(endIndex);

        return before + "\n" + content + "\n" + after;
    }
}
