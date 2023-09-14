# About the Project

A Java Application for retrieving Gmail emails content via IMAP using JavaMail.

This application will search the Inbox folder for an email given a subject. If found, it will mark the email as read and delete it.

## Getting Started

### Prerequisites
- OpenJDK 17+
- Apache Maven 3.9.3+
- A Gmail account
- An OAuth2 access token

### Getting the OAuth2 token
 In order to get an OAuth2 token, you will need to configure a Google project on Google developer's console. A detailed step and instructions can be found [here](http://code.google.com/apis/accounts/docs/OAuth2.html).
 In particular, you will need the Client ID, the Client secret and the refresh token.

 A very easy way to obtain the refresh token is using the tool [OAuth2DotPyRunThrough](https://github.com/google/gmail-oauth2-tools/wiki/OAuth2DotPyRunThrough).
 
 After getting the refresh token, the Access token can be obtained and updated by performing a POST request to the endpoint https://oauth2.googleapis.com/token with the parameters ```client_id```, ```client_secret```, ```grant_type``` and the ```refresh_token```.
 
 E.g:

```
curl --location --request POST 'https://oauth2.googleapis.com/token?client_id=GOOGLE_CLIENT_ID&client_secret=GOOGLE_CLIENT_SECRET&grant_type=refresh_token&refresh_token=REFRESH_TOKEN'
```
### Notes

- It is worth mention that the Access token will only last 1h. After that period you will need to make a new POST request to get a new one.

- From the Google [documentation](https://developers.google.com/identity/protocols/oauth2#expiration) the refresh token will expire in 7 days if the Google project is set to "Testing". So, if you want the token to never expire, you can change your App status to "Published".

## Running the project

After getting the necessary tokens, you will need yo update the file ```api.properties``` to use them:

```
GoogleApiUri=https://oauth2.googleapis.com
ClientGoogleId=YOUR_GOOGLE_CLIENT_ID
ClientGoogleSecret=YOUR_GOOGLE_CLIENT_SECRET
ClientGoogleRefreshToken=YOUR_GOOGLE_REFRESH_TOKEN
```
There is a ```EmailExample.java``` class that can be used to validate if everything is set correctly, you just need to update the ```YOUR_EMAIL``` and ```EMAIL_SUBJECT``` constants to reflect the ones you want.

E.g:

This will search the Inbox folder from the email "myemail@gmail.com" that contains the subject "My Email Subject". Assuming "myemail@gmail.com" is the account that the Google project was set on, it should print the contents of the email.

```
package com.email.example.main;

import com.email.example.support.email.gmail.ImapGmailEmailClient;
import com.email.example.support.helpers.EmailHelper;
import com.email.example.support.email.EmailClient;

public class EmailExample {
    public static void main(String[] args) throws InterruptedException {
        EmailClient imapGmailEmailClient = new ImapGmailEmailClient("myemail@gmail.com");
        String content = new EmailHelper(imapGmailEmailClient).getEmailContents("My email Subject");
        System.out.println(content);
    }
}
```

## References
- [JavaMail Documentation](https://javaee.github.io/javamail/OAuth2)
- [Google Documentation](https://developers.google.com/identity/protocols/oauth2)
