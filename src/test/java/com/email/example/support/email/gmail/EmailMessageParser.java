package com.email.example.support.email.gmail;

import com.email.example.support.email.EmailMessage;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import java.io.IOException;

public class EmailMessageParser {

    public static EmailMessage parseMessage(Message message) {
        try {
            return new EmailMessage(message.getSubject(), parseContents(message), message.getSentDate());
        } catch (MessagingException e) {
            throw new EmailMessageParserException(e);
        } catch (IOException e) {
            throw new EmailMessageParserException(e);
        }
    }

    private static String parseContents(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return parseTextMimetype(part);
        } else if (part.isMimeType("multipart/alternative")) {
            return parseMultipartAlternativeMimeType(part);
        } else if (part.isMimeType("multipart/*")) {
            return parseMultipartMimetype(part);
        } else {
            throw new EmailMessageContentException("Error returning email contents");
        }
    }

    private static String parseMultipartMimetype(Part p) throws IOException, MessagingException {
        Multipart mp = (Multipart) p.getContent();
        for (int i = 0; i < mp.getCount(); i++) {
            String s = parseContents(mp.getBodyPart(i));
            if (s != null)
                return s;
        }
        throw new EmailMessageMultipartException("Error returning email multipart content");
    }

    private static String parseMultipartAlternativeMimeType(Part p) throws IOException, MessagingException {
        Multipart mp = (Multipart) p.getContent();
        String text = null;
        for (int i = 0; i < mp.getCount(); i++) {
            Part bp = mp.getBodyPart(i);
            if (bp.isMimeType("text/plain")) {
                if (text == null)
                    text = parseContents(bp);
            } else if (bp.isMimeType("text/html")) {
                String s = parseContents(bp);
                if (s != null)
                    return s;
            } else {
                return parseContents(bp);
            }
        }
        return text;
    }

    private static String parseTextMimetype(Part p) throws IOException, MessagingException {
        return (String) p.getContent();
    }

}
