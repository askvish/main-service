package com.google.main.service;

import org.springframework.stereotype.Service;

@Service
public class JokeService {

    private final APIClient apiClient;

    public JokeService(APIClient apiClient) {
        this.apiClient = apiClient;
    }

    public String fetchData() {
        return apiClient.getData();
    }
}
