package com.google.main.controller;

import com.google.main.service.GMailService;

import com.google.main.to.GmailMessageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
public class GMailController {

    private final GMailService gMailService;

    public GMailController(GMailService gMailService) {
        this.gMailService = gMailService;
    }

    @GetMapping("/gmail/messages")
    public List<GmailMessageDto> getMessages(@RequestParam(defaultValue = "20") int maxResults)
            throws Exception {
        return gMailService.getInboxMessages(maxResults);
    }
}