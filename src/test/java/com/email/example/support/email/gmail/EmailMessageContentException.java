package com.email.example.support.email.gmail;

public class EmailMessageContentException extends RuntimeException {
    public EmailMessageContentException(String error_message) {
        super(error_message);
    }
}
