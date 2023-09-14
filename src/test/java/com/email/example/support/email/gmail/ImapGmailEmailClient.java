package com.email.example.support.email.gmail;

import com.email.example.support.properties.ApiProperties;
import com.email.example.support.email.EmailClient;
import com.email.example.support.email.EmailMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ImapGmailEmailClient implements EmailClient {

    private static final String GMAIL_ADDRESS = "imap.gmail.com";
    private static final String STORE_TYPE = "imap";

    private final String emailAddress;
    private final String authenticationToken;

    public ImapGmailEmailClient(String emailAddress) {
        this.emailAddress = emailAddress;
        this.authenticationToken = getGoogleToken();
    }

    @Override
    public List<EmailMessage> getMessages(String folder,
                                            String subject) {
        try (Store store = connect()) {
            Folder mailbox = store.getFolder(folder);
            try {
                mailbox.open(Folder.READ_WRITE);
                Message[] messages = searchUnseenMessages(mailbox);
                return parseMessages(messages, subject);
            } finally {
                mailbox.close();
            }
        } catch (MessagingException e) {
            throw new GmailClientConnectionException(e);
        }
    }

    private Message[] searchUnseenMessages(Folder mailbox) {
        try {
            return mailbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        } catch (MessagingException e) {
            throw new GmailClientSearchMailboxException(e);
        }
    }

    private List<EmailMessage> parseMessages(Message[] messages,
                                               String subject) {
        return Arrays.stream(messages)
                .filter(m -> getMessageSubject(m).contains(subject))
                .map(this::setSeenAndDelete)
                .map(this::parseMessage)
                .toList();
    }

    private EmailMessage parseMessage(Message message) {
        return EmailMessageParser.parseMessage(message);
    }

    private Message setSeenAndDelete(Message message) {
        try {
            setSeen(message);
            setDeleted(message);
            return message;
        } catch (MessagingException e) {
            throw new EmailMessageSeenDeleteException(e);
        }
    }

    private void setSeen(Message message) throws MessagingException {
        message.setFlags(new Flags(Flags.Flag.SEEN), true);
    }

    private void setDeleted(Message message) throws MessagingException {
        message.setFlags(new Flags(Flags.Flag.DELETED), true);
    }

    private String getMessageSubject(Message message) {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            throw new GmailClientMessageParseException(e);
        }
    }

    private Store connect() {
        try {
            Properties properties = setupProperties();
            Session session = Session.getInstance(properties);
            Store store = session.getStore(STORE_TYPE);
            store.connect(GMAIL_ADDRESS, emailAddress, authenticationToken);
            return store;
        } catch (MessagingException e) {
            throw new GmailClientConnectionException(e);
        }
    }

    private static Properties setupProperties() {
        Properties props = new Properties();
        props.put(ClientProperties.SSL_ENABLE.getKey(), ClientProperties.SSL_ENABLE.getValue());
        props.put(ClientProperties.AUTH_MECHANISMS.getKey(), ClientProperties.AUTH_MECHANISMS.getValue());
        return props;
    }

    private static String getGoogleToken() {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost(ApiProperties.GOOGLE_API_URI.value() + "/token");

            List<NameValuePair> arguments = new ArrayList<>(4);
            arguments.add(new BasicNameValuePair(ClientProperties.CLIENT_ID.getKey(), ClientProperties.CLIENT_ID.getValue()));
            arguments.add(new BasicNameValuePair(ClientProperties.CLIENT_SECRET.getKey(), ClientProperties.CLIENT_SECRET.getValue()));
            arguments.add(new BasicNameValuePair(ClientProperties.GRANT_TYPE.getKey(), ClientProperties.GRANT_TYPE.getValue()));
            arguments.add(new BasicNameValuePair(ClientProperties.REFRESH_TOKEN.getKey(), ClientProperties.REFRESH_TOKEN.getValue()));

            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);
            JSONObject obj = new JSONObject(EntityUtils.toString(response.getEntity()));
            return obj.getString("access_token");

        } catch (IOException e) {
            throw new GmailClientTokenException(e);
        }
    }

    private enum ClientProperties {
        SSL_ENABLE("mail.imap.ssl.enable", "true"),
        AUTH_MECHANISMS("mail.imap.auth.mechanisms", "XOAUTH2"),
        CLIENT_ID("client_id", ApiProperties.CLIENTE_GOOGLE_ID.value()),
        CLIENT_SECRET("client_secret", ApiProperties.CLIENTE_GOOGLE_SECRET.value()),
        REFRESH_TOKEN("refresh_token", ApiProperties.CLIENTE_GOOGLE_REFRESH_TOKEN.value()),
        GRANT_TYPE("grant_type", "refresh_token");

        private final String key;
        private final String value;

        ClientProperties(String key,
                         String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

}
