package com.google.main.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class APIClient {

    private final RestClient restClient;

    public APIClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://official-joke-api.appspot.com")
                .build();
    }

    public String getData() {
        return restClient.get()
                .uri("/jokes/programming/random")
                .retrieve()
                .body(String.class);
    }
}
