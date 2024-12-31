package com.marceloluiz.exchangerate.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ExchangeRateProperties {
    @Value("${BASE_CURRENCY}") // Default city
    private String baseCurrency;

    @Value("${TARGET_CURRENCY}") // Default forecast days
    private String targetCurrency;
}