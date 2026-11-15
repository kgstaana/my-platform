package org.vegetabs.iamservice.user.application.out;

import org.vegetabs.iamservice.user.domain.model.User;

public interface EmailVerificationPort {

    void send(User user, String rawToken);
}
