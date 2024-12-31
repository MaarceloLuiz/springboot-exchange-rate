package com.marceloluiz.exchangerate.deserializers;

import com.marceloluiz.exchangerate.config.ApiConfiguration;
import com.marceloluiz.exchangerate.config.converters.ConvertData;
import com.marceloluiz.exchangerate.model.dto.ExchangeRateDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDeserializer {
    private static String fetchJson(String date, String baseCurrency, String targetCurrency){
        return "https://api.frankfurter.dev/v1/"
                + date
                + "?base=" + baseCurrency
                + "&symbols=" + targetCurrency;
    }

    public static List<ExchangeRateDTO> getMonthlyData(String baseCurrency, String targetCurrency){
        ApiConfiguration apiConfiguration = new ApiConfiguration();
        ConvertData convert = new ConvertData();;
        ExchangeRateDTO data;
        List<ExchangeRateDTO> dataList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for(int i = 0; i < 30; i++){
            LocalDate previousDates = today.minusDays(i);
            String json = apiConfiguration.getData(fetchJson(previousDates.toString(), baseCurrency, targetCurrency));

            data = convert.getData(json, ExchangeRateDTO.class);
            dataList.add(data);
        }

        return dataList;
    }
}
