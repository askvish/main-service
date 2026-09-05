package com.google.main.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

import com.google.main.to.GmailMessageDto;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

//@Service
public class GMailService {

    private static final String APPLICATION_NAME =
            "Mail Service";

    private static final GsonFactory JSON_FACTORY =
            GsonFactory.getDefaultInstance();

    /*
     * credentials.json:
     *
     * src/main/resources/credentials.json
     */
    private static final String CREDENTIALS_FILE =
            "credentials.json";

    /*
     * Local directory where OAuth tokens are stored.
     */
    private static final String TOKENS_DIRECTORY =
            "D:\\Home\\SpringBoot Backend\\mail\\tokens";

    /*
     * Must exactly match Google Cloud Console.
     */
    private static final String REDIRECT_URI =
            "https://localhost:9000/oauth2/callback";

    /*
     * This is ONLY the local key used to store/load
     * the OAuth credential.
     */
    private static final String CREDENTIAL_USER_ID =
            "user";

    /*
     * IMPORTANT:
     *
     * This is the Gmail API user ID.
     *
     * "me" means the Gmail account represented by
     * the OAuth access token.
     */
    private static final String GMAIL_USER_ID =
            "me";

    private static final List<String> SCOPES =
            Collections.singletonList(
                    GmailScopes.GMAIL_READONLY
            );

    private final NetHttpTransport transport;

    private final GoogleAuthorizationCodeFlow flow;

    public GMailService() throws GeneralSecurityException, IOException {
        this.transport = GoogleNetHttpTransport.newTrustedTransport();
        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                        transport,
                        JSON_FACTORY,
                        new GoogleClientSecrets(),
                        SCOPES
                )
                        .setDataStoreFactory(
                                new FileDataStoreFactory(
                                        new File("")
                                )
                        )
                        .setAccessType("offline")
                        .build();
    }

//    public GMailService() throws Exception {
//
//        this.transport =
//                GoogleNetHttpTransport.newTrustedTransport();
//
//        GoogleClientSecrets clientSecrets =
//                loadClientSecrets();
//
//        File tokenDirectory =
//                new File(TOKENS_DIRECTORY);
//
//        if (!tokenDirectory.exists()) {
//
//            if (!tokenDirectory.mkdirs()) {
//
//                throw new IOException(
//                        "Unable to create token directory: "
//                                + tokenDirectory.getAbsolutePath()
//                );
//            }
//        }
//
//        this.flow =
//                new GoogleAuthorizationCodeFlow.Builder(
//                        transport,
//                        JSON_FACTORY,
//                        clientSecrets,
//                        SCOPES
//                )
//                        .setDataStoreFactory(
//                                new FileDataStoreFactory(
//                                        tokenDirectory
//                                )
//                        )
//                        .setAccessType("offline")
//                        .build();
//    }

    /**
     * Load credentials.json from:
     * <p>
     * src/main/resources/credentials.json
     */
    private GoogleClientSecrets loadClientSecrets()
            throws Exception {

        InputStream input =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                                CREDENTIALS_FILE
                        );

        if (input == null) {

            throw new IllegalStateException(
                    "credentials.json was not found in "
                            + "src/main/resources"
            );
        }

        try (InputStreamReader reader =
                     new InputStreamReader(
                             input,
                             StandardCharsets.UTF_8
                     )) {

            return GoogleClientSecrets.load(
                    JSON_FACTORY,
                    reader
            );
        }
    }

    /**
     * Generate Google OAuth authorization URL.
     */
    public String getAuthorizationUrl() {

        return flow
                .newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .setAccessType("offline")
                .build();
    }

    /**
     * Exchange authorization code for
     * access token + refresh token.
     */
    public void exchangeCode(String code)
            throws Exception {

        if (code == null || code.isBlank()) {

            throw new IllegalArgumentException(
                    "Authorization code cannot be empty."
            );
        }

        var tokenResponse =
                flow.newTokenRequest(code)
                        .setRedirectUri(REDIRECT_URI)
                        .execute();

        if (tokenResponse == null) {

            throw new IllegalStateException(
                    "Google did not return a token response."
            );
        }

        Credential credential =
                flow.createAndStoreCredential(
                        tokenResponse,
                        CREDENTIAL_USER_ID
                );

        if (credential == null) {

            throw new IllegalStateException(
                    "Could not create Google OAuth credential."
            );
        }

        System.out.println(
                "Google OAuth authorization completed successfully."
        );

        System.out.println(
                "Google OAuth credential stored."
        );
    }

    /**
     * Check whether OAuth credentials exist.
     */
    public boolean isAuthorized()
            throws Exception {

        Credential credential =
                flow.loadCredential(
                        CREDENTIAL_USER_ID
                );

        return credential != null;
    }

    /**
     * Create authenticated Gmail client.
     */
    private Gmail getGmailClient()
            throws Exception {

        Credential credential =
                flow.loadCredential(
                        CREDENTIAL_USER_ID
                );

        if (credential == null) {

            throw new IllegalStateException(
                    "Gmail is not authorized. "
                            + "Visit "
                            + "https://localhost:9000/oauth2/authorize "
                            + "first."
            );
        }

        /*
         * If the access token has expired, the Google
         * client can use the refresh token automatically.
         */
        if (credential.getAccessToken() == null
                && credential.getRefreshToken() == null) {

            throw new IllegalStateException(
                    "No valid Google access or refresh token found. "
                            + "Authorize Gmail again."
            );
        }

        return new Gmail.Builder(
                transport,
                JSON_FACTORY,
                credential
        )
                .setApplicationName(
                        APPLICATION_NAME
                )
                .build();
    }

    /**
     * Get inbox messages.
     * <p>
     * IMPORTANT:
     * <p>
     * Uses "me", NOT "user".
     */
    public List<GmailMessageDto> getInboxMessages(
            int maxResults
    ) throws Exception {

        if (maxResults <= 0) {

            throw new IllegalArgumentException(
                    "maxResults must be greater than zero."
            );
        }

        Gmail gmail =
                getGmailClient();

        /*
         * CORRECT:
         *
         * users/me/messages
         *
         * NOT:
         *
         * users/user/messages
         */
        ListMessagesResponse response =
                gmail.users()
                        .messages()
                        .list(GMAIL_USER_ID)
                        .setLabelIds(
                                List.of("INBOX")
                        )
                        .setMaxResults(
                                (long) maxResults
                        )
                        .execute();

        if (response == null
                || response.getMessages() == null) {

            return List.of();
        }

        /*
         * messages.list() only returns IDs.
         *
         * For every ID we call messages.get()
         * to retrieve the complete email.
         */
        return response
                .getMessages()
                .stream()
                .map(message -> {

                    try {

                        return getFullMessage(
                                gmail,
                                message.getId()
                        );

                    } catch (Exception e) {

                        throw new RuntimeException(
                                "Failed to read Gmail message: "
                                        + message.getId(),
                                e
                        );
                    }

                })
                .toList();
    }

    /**
     * Retrieve complete Gmail message.
     */
    private GmailMessageDto getFullMessage(Gmail gmail, String messageId) throws Exception {
        Message message = gmail.users().messages()
                .get(GMAIL_USER_ID, messageId)
                .setFormat("full").execute();
        return convertToDto(message);
    }

    /**
     * Read a Gmail message header.
     */
    private String getHeader(
            Message message,
            String headerName
    ) {

        if (message.getPayload() == null
                || message.getPayload().getHeaders() == null) {

            return "";
        }

        return message
                .getPayload()
                .getHeaders()
                .stream()
                .filter(header ->
                        headerName.equalsIgnoreCase(
                                header.getName()
                        )
                )
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse("");
    }

    /**
     * Extract email body.
     */
    private String extractBody(
            Message message
    ) {

        if (message.getPayload() == null) {

            return "";
        }

        return extractBodyFromPart(
                message.getPayload()
        );
    }

    /**
     * Recursively extract text/html or text/plain.
     */
    private String extractBodyFromPart(
            MessagePart part
    ) {

        if (part == null) {

            return "";
        }

        String mimeType =
                part.getMimeType();

        /*
         * Direct text/html body.
         */
        if ("text/html".equalsIgnoreCase(mimeType)) {

            if (part.getBody() != null
                    && part.getBody().getData() != null) {

                return decodeBase64Url(
                        part.getBody().getData()
                );
            }
        }

        /*
         * Direct text/plain body.
         */
        if ("text/plain".equalsIgnoreCase(mimeType)) {

            if (part.getBody() != null
                    && part.getBody().getData() != null) {

                return decodeBase64Url(
                        part.getBody().getData()
                );
            }
        }

        /*
         * Multipart email.
         */
        if (part.getParts() != null) {

            /*
             * First preference: HTML.
             */
            for (MessagePart child :
                    part.getParts()) {

                if ("text/html".equalsIgnoreCase(
                        child.getMimeType()
                )) {

                    String html =
                            extractBodyFromPart(
                                    child
                            );

                    if (!html.isBlank()) {

                        return html;
                    }
                }
            }

            /*
             * Second preference: plain text.
             */
            for (MessagePart child :
                    part.getParts()) {

                if ("text/plain".equalsIgnoreCase(
                        child.getMimeType()
                )) {

                    String text =
                            extractBodyFromPart(
                                    child
                            );

                    if (!text.isBlank()) {

                        return text;
                    }
                }
            }

            /*
             * Recursively search nested multipart sections.
             */
            for (MessagePart child :
                    part.getParts()) {

                String body =
                        extractBodyFromPart(
                                child
                        );

                if (!body.isBlank()) {

                    return body;
                }
            }
        }

        return "";
    }

    /**
     * Gmail uses Base64 URL encoding for message bodies.
     */
    private String decodeBase64Url(
            String data
    ) {

        byte[] decoded =
                java.util.Base64
                        .getUrlDecoder()
                        .decode(data);

        return new String(
                decoded,
                StandardCharsets.UTF_8
        );
    }

    /**
     * Get one complete Gmail message. * * GET: * /gmail/v1/users/me/messages/{messageId}
     */
    public GmailMessageDto getMessage(String messageId) throws Exception {
        if (messageId == null || messageId.isBlank()) {
            throw new IllegalArgumentException("Message ID cannot be empty.");
        }
        Gmail gmail = getGmailClient();
        Message message = gmail.users().messages().get(GMAIL_USER_ID, messageId)
                .setFormat("full").execute();
        return convertToDto(message);
    }

    private GmailMessageDto convertToDto(
            Message message
    ) {

        String from =
                getHeader(
                        message,
                        "From"
                );

        String to =
                getHeader(
                        message,
                        "To"
                );

        String subject =
                getHeader(
                        message,
                        "Subject"
                );

        String date =
                getHeader(
                        message,
                        "Date"
                );

        String body =
                extractBody(message);

        return new GmailMessageDto(
                message.getId(),
                message.getThreadId(),
                from,
                to,
                subject,
                date,
                message.getSnippet(),
                body
        );
    }

}