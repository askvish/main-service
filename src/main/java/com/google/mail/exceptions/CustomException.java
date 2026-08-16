package com.google.mail.exceptions;

public class CustomException extends Exception{

    private final String errorMessage;

    public CustomException(String message, String errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
