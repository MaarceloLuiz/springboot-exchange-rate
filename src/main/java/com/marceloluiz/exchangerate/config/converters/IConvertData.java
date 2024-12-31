package com.marceloluiz.exchangerate.config.converters;

public interface IConvertData {
    <T> T getData(String json, Class<T> javaClass);
}
