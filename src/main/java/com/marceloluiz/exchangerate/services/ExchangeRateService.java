package com.marceloluiz.exchangerate.services;

import com.marceloluiz.exchangerate.config.ExchangeRateProperties;
import com.marceloluiz.exchangerate.deserializers.ExchangeRateDeserializer;
import com.marceloluiz.exchangerate.model.ExchangeRate;
import com.marceloluiz.exchangerate.model.dto.ExchangeRateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateProperties properties;

    public List<ExchangeRate> getExchangeRate(){
        List<ExchangeRate> rate = new ArrayList<>();
        for(ExchangeRateDTO dto : ExchangeRateDeserializer.getMonthlyData(properties.getBaseCurrency(), properties.getTargetCurrency())){
            rate.add(ExchangeRate.valueOf(dto.getBase(), dto.getDate(), dto.getRates()));
        }

        return rate;
    }
}
