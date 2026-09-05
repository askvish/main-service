package com.google.main.controller;

import com.google.main.service.GMailService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/oauth2")
public class GoogleOAuthController {

    private final GMailService gmailService;

    public GoogleOAuthController(GMailService gmailService) {
        this.gmailService = gmailService;
    }

    @GetMapping("/authorize")
    public String authorize() {

        String authorizationUrl =
                gmailService.getAuthorizationUrl();

        return """
                <html>
                    <body>
                        <h2>Google Gmail Authorization</h2>
                        <a href="%s">
                            Authorize Gmail Access
                        </a>
                    </body>
                </html>
                """.formatted(authorizationUrl);
    }

    @GetMapping("/callback")
    public String callback(
            @RequestParam String code
    ) throws Exception {

        gmailService.exchangeCode(code);

        return """
                <html>
                    <body>
                        <h2>Authorization Successful</h2>
                        <p>Gmail access has been authorized.</p>
                        <p>
                            You can now call:
                            <a href="/gmail/messages">
                                /gmail/messages
                            </a>
                        </p>
                    </body>
                </html>
                """;
    }
}