package com.email.example.support.email.gmail;

public class EmailMessageSeenDeleteException extends RuntimeException {
    public EmailMessageSeenDeleteException(Throwable throwable) {
        super(throwable);
    }
}
