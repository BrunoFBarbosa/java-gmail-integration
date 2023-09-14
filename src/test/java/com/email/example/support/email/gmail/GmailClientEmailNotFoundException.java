package com.email.example.support.email.gmail;

public class GmailClientEmailNotFoundException extends RuntimeException {
    public GmailClientEmailNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
