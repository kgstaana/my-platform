package org.vegetabs.iamservice.user.adapter.out.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vegetabs.iamservice.user.application.out.EmailVerificationPort;
import org.vegetabs.iamservice.user.domain.model.User;

@Slf4j
@Component
public class EmailVerificationAdapter implements EmailVerificationPort {

    @Override
    public void send(User user, String rawToken) {
        log.info(
            "Sending email verification notification. userId={}, email={}",
            user.id(),
            user.email()
        );

        try {
            // Call notification service

            log.info(
                "Email verification notification successfully requested. userId={}",
                user.id()
            );
        } catch (Exception ex) {
            log.error(
                "Failed to send email verification notification. userId={}, error={}",
                user.id(),
                ex.getMessage(),
                ex
            );
            throw ex;
        }
    }
}
