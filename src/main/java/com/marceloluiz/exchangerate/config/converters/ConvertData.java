package com.marceloluiz.exchangerate.config.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData implements IConvertData{
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> javaClass) {
        try{
            return mapper.readValue(json,javaClass);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
