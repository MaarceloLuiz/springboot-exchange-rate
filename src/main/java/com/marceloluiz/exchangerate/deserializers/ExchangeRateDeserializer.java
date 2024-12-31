package com.marceloluiz.exchangerate.deserializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marceloluiz.exchangerate.config.ApiConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class ExchangeRateDeserializer {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ApiConfiguration apiConfiguration;
    private JsonNode jsonNode;
}
