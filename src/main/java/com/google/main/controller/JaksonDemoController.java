package com.google.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.main.service.JaksonDemoService;
import com.google.main.to.ResponseTO;
import com.google.main.to.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jackson")
public class JaksonDemoController {

    private final JaksonDemoService jaksonDemoService;

    private final ObjectMapper objectMapper;

    public JaksonDemoController(JaksonDemoService jaksonDemoService, ObjectMapper objectMapper) {
        this.jaksonDemoService = jaksonDemoService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/user")
    ResponseEntity<ResponseTO<List<User>>> getUsers() throws JsonProcessingException {

        List<User> users = new ArrayList<>(List.of(
                new User(1, "Ashok", "ashok@mail.com"),
                new User(2, "Ram", "ram@google.com"),
                new User(3, "null", "shyam@gmail.com")
        ));

        User aditi = new User(5, "Aditi", "aditi@mail.com");
        users.add(aditi);

        String jsonString = """
                {
                    "userId": 4,
                    "name": "Hello",
                    "email": "hello@mail.com"
                }
                """;

        User user = objectMapper.readValue(jsonString, User.class);
        users.add(user);

        return ResponseEntity.ok(
                new ResponseTO<>("success", users));
    }

    @GetMapping("/status-codes")
    ResponseEntity<ResponseTO<Map<String, Object>>> getStatusCodes() {

        Map<String, Object> map = jaksonDemoService.getStatusCodesMap();

        return ResponseTO.handleResponse(map);
    }


}
