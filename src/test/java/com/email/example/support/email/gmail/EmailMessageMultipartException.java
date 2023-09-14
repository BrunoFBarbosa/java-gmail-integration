package com.email.example.support.email.gmail;

public class EmailMessageMultipartException extends RuntimeException {
    public EmailMessageMultipartException(String error_message) {
        super(error_message);
    }
}
