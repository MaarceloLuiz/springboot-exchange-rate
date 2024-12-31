package com.marceloluiz.exchangerate.config;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Configuration
public class ApiConfiguration {

    public String getData(String url){
        try{
            URI address = URI.create(url);
            try(var client = HttpClient.newHttpClient()){
                var request = HttpRequest.newBuilder(address).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();
            }
        }catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}
