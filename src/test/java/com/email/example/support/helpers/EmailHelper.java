package com.email.example.support.helpers;

import com.email.example.support.email.gmail.GmailClientEmailNotFoundException;
import com.email.example.support.email.EmailClient;
import com.email.example.support.email.EmailMessage;

public class EmailHelper {

    private static final int MAX_RETRIES = 6;
    private static final long WAIT_TIME = 5000;

    private final EmailClient client;

    public EmailHelper(EmailClient client) {
        this.client = client;
    }

    public String getEmailContents(String subject) throws InterruptedException {

        int currentRetries = 0;
        while (currentRetries < MAX_RETRIES) {
            try {
                EmailMessage emailMessage = client.getMessages("Inbox", subject)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new GmailClientEmailNotFoundException("Email not found"));

                 return emailMessage.contents();
            } catch (RuntimeException e) {
                currentRetries++;
                Thread.sleep(WAIT_TIME);
            }
        }

        throw new GmailClientEmailNotFoundException("Email not found");

    }

}
