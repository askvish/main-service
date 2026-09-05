package com.google.main.controller;

import com.google.main.to.GmailMessageDto;
import com.google.main.service.GMailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@Controller
//@RequestMapping("/mail")
public class MailViewController {

    private final GMailService gmailService;

    public MailViewController(GMailService gmailService) {
        this.gmailService = gmailService;
    }

    /**
     * Inbox
     *
     * GET /mail
     */
    @GetMapping
    public String inbox(Model model) throws Exception {

        var messages =
                gmailService.getInboxMessages(20);

        model.addAttribute(
                "messages",
                messages
        );

        return "mail";
    }

    /**
     * View one email
     *
     * GET /mail/view?id=xxxxx
     */
    @GetMapping("/view")
    public String viewMail(
            @RequestParam("id") String id,
            Model model
    ) throws Exception {

        if (id == null || id.isBlank()) {

            throw new IllegalArgumentException(
                    "Gmail message ID is required."
            );
        }

        GmailMessageDto message =
                gmailService.getMessage(id);

        if (message == null) {

            throw new IllegalStateException(
                    "Gmail message not found: " + id
            );
        }

        model.addAttribute(
                "message",
                message
        );

        return "mail-detail";
    }
}