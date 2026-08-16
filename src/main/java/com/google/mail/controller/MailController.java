package com.google.mail.controller;

import com.google.mail.to.MailTO;
import com.google.mail.to.ResponseTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);
    private final List<MailTO> sentMails = new ArrayList<>();

    @GetMapping("/ping")
    public String ping() {
        logger.info("[PING] Health check endpoint called");
        String response = "Mail service is running!";
        logger.debug("[PING] Response: {}", response);
        return response;
    }

    @GetMapping("/sent")
    public ResponseEntity<ResponseTO<List<MailTO>>> getSentMails() {
        logger.info("[GET /sent] Retrieving all sent mails");
        logger.debug("[GET /sent] Total mails in storage: {}", sentMails.size());
        logger.info("[GET /sent] Successfully retrieved {} mails", sentMails.size());
        return ResponseEntity.ok(new ResponseTO<>("success", sentMails));
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody MailTO mailTO) {
        logger.info("[POST /send] Attempting to send mail");
        logger.debug("[POST /send] Mail details - To: {}, Subject: {}, Body: {}",
                mailTO.getTo(), mailTO.getSubject(), mailTO.getBody());
        try {
            sentMails.add(mailTO);
            logger.info("[POST /send] Mail sent successfully. Total mails: {}", sentMails.size());
            return ResponseEntity.ok("Mail sent successfully!");
        } catch (Exception e) {
            logger.error("[POST /send] Error sending mail", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending mail: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity<String> deleteMail(@PathVariable int index) {
        logger.info("[DELETE /delete] Attempting to delete mail at index: {}", index);
        logger.debug("[DELETE /delete] Current total mails: {}", sentMails.size());

        if (index < 0 || index >= sentMails.size()) {
            logger.warn("[DELETE /delete] Invalid index {}. Valid range: 0-{}", index, sentMails.size() - 1);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Mail not found!");
        }

        MailTO deletedMail = sentMails.get(index);
        sentMails.remove(index);
        logger.info("[DELETE /delete] Mail at index {} deleted successfully. Remaining mails: {}", index, sentMails.size());
        logger.debug("[DELETE /delete] Deleted mail - To: {}, Subject: {}", deletedMail.getTo(), deletedMail.getSubject());

        return ResponseEntity.ok("Mail deleted successfully!");
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllMails() {
        logger.info("[DELETE /deleteAll] Attempting to delete all mails");
        int mailCountBeforeClear = sentMails.size();
        logger.debug("[DELETE /deleteAll] Mails to be deleted: {}", mailCountBeforeClear);

        sentMails.clear();
        logger.info("[DELETE /deleteAll] Successfully deleted all {} mails", mailCountBeforeClear);

        return ResponseEntity.ok("All mails deleted successfully!");
    }
}