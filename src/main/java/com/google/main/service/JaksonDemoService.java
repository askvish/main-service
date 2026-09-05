package com.google.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JaksonDemoService {

    public String getUsers() {
        return "Jakson";
    }

    public @NonNull Map<String, Object> getStatusCodesMap() {

        int sum = 0;
        for (int i = 1; i <= 10_000_000; i++) {
            sum += i;
        }

        Map<String, Object> map = new LinkedHashMap<>();
        HttpStatus[] codes = HttpStatus.values();

        Arrays.stream(codes).forEach(
                code -> map.put(
                        String.valueOf(code.value()),
                        code.name() + " (" + code.getReasonPhrase() + ")"
                )
        );
        return map;
    }
}
