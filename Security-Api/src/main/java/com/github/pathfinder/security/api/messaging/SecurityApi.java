package com.github.pathfinder.security.api.messaging;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.router.IMessageRouter;
import com.github.pathfinder.security.api.message.IsValidCredentialsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityApi {

    private final IMessageRouter messageRouter;

    @Logged
    public boolean isValid(String username, String password) {
        return messageRouter
                .message(SecurityQueues.SECURITY_QUEUE)
                .with(new IsValidCredentialsMessage(username, password))
                .sendAndReceive(Boolean.class);
    }

}
