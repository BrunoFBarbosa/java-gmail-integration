package com.email.example.support.email.gmail;

public class GmailClientConnectionException extends RuntimeException {
    public GmailClientConnectionException(Throwable thowable) {
        super(thowable);
    }
}
