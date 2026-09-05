//package com.google.mail.service;
//
//import com.openai.client.OpenAIClient;
//import com.openai.models.responses.Response;
//import com.openai.models.responses.ResponseCreateParams;
//import com.openai.models.responses.ResponseOutputText;
//import org.springframework.stereotype.Service;
//
//import java.util.stream.Collectors;
//
//@Service
//public class OpenAIService {
//
//    private final OpenAIClient client;
//
//    public OpenAIService(OpenAIClient client) {
//        this.client = client;
//    }
//
//    public String ask(String prompt) {
//
//        ResponseCreateParams params = ResponseCreateParams.builder()
//                .model("gpt-5-nano")
//                .input(prompt)
//                .build();
//
//        Response response = client.responses().create(params);
//
//        String collect = response.output().stream()
//                .flatMap(item -> item.message().stream())
//                .flatMap(message -> message.content().stream())
//                .flatMap(content -> content.outputText().stream())
//                .map(ResponseOutputText::text)
//                .collect(Collectors.joining());
//
//        System.out.println(collect);
//
//        return collect;
//    }
//}
