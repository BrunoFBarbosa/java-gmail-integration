package com.email.example.main;

import com.email.example.support.email.gmail.ImapGmailEmailClient;
import com.email.example.support.helpers.EmailHelper;
import com.email.example.support.email.EmailClient;

public class EmailExample {
    public static void main(String[] args) throws InterruptedException {
        EmailClient imapGmailEmailClient = new ImapGmailEmailClient(YOUR_EMAIL);
        String content = new EmailHelper(imapGmailEmailClient).getEmailContents(EMAIL_SUBJECT);
        System.out.println(content);
    }
}
