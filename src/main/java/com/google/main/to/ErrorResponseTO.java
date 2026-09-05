package com.google.main.to;

import java.time.LocalDateTime;

public record ErrorResponseTO(
        LocalDateTime timeStamp,
        int status,
        String error,
        String message,
        String path
) {
}
