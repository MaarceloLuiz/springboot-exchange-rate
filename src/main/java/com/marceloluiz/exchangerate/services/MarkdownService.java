package com.marceloluiz.exchangerate.services;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MarkdownService {
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);

    public String generateChartMarkdown(String chartPath){
        StringBuilder markdown = new StringBuilder();
        markdown.append("""
                ## Exchange Rate Chart
                
                ![Exchange Rate Chart](""").append(chartPath).append(")");
        generateUpdatedAt(markdown);
        return markdown.toString();
    }

    private void generateUpdatedAt(StringBuilder markdown){
        markdown.append("*Updated at: ")
                .append(generateTimestamp())
                .append(" by [MaarceloLuiz/springboot-exchange-rate](https://github.com/MaarceloLuiz/springboot-exchange-rate)")
                .append("*\n\n");
    }

    private String generateTimestamp(){
        return ZonedDateTime.now().format(FORMATTER);
    }
}
