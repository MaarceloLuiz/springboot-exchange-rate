package com.marceloluiz.exchangerate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRate {
    private String base;
    private String date;
    private Map<String, Double> rates;

    public static ExchangeRate valueOf(String base, String date, Map<String, Double> rates){
        return new ExchangeRate(base, date, rates);
    }

    private ExchangeRate(String base, String date, Map<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }
}
