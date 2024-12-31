package com.marceloluiz.exchangerate.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FileProperties {
    private final String README_PATH = "README.md";
    private final String CHART_IMAGE_PATH = "charts/chart.png";
}
