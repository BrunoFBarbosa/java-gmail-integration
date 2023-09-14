package com.email.example.support.email;

import java.util.Date;

public record EmailMessage(String subject,
                           String contents,
                           Date timestamp) {
}
