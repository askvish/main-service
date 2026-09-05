package com.google.main.controller;

import com.google.main.service.AIService;
//import com.google.mail.service.OpenAIService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin("*")
public class OpenAIController {
    //    private final OpenAIService openAIService;
    private final AIService aiService;

    public OpenAIController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message) {
//        return openAIService.ask(prompt);
        return aiService.stream(message);
    }
}
