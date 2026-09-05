package com.google.main.to;

public record GmailMessageDto(
        String id,
        String threadId,
        String from,
        String to,
        String subject,
        String date,
        String snippet,
        String body
) {
}
