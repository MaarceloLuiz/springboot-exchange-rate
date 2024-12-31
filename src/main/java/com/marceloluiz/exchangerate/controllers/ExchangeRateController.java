package com.marceloluiz.exchangerate.controllers;

import com.marceloluiz.exchangerate.services.ExchangeRateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rate")
@AllArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;
}
