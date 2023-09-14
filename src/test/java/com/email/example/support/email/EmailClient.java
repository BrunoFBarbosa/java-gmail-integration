package com.email.example.support.email;

import java.util.List;

public interface EmailClient {
    List<EmailMessage> getMessages(String folder,
                                   String subject);
}
