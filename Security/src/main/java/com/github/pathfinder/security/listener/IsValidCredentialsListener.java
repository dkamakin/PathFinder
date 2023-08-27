package com.github.pathfinder.security.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.annotation.Listener;
import com.github.pathfinder.security.api.messaging.SecurityQueues;
import com.github.pathfinder.security.api.message.IsValidCredentialsMessage;
import com.github.pathfinder.security.service.ISecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * TODO: Add a proper response
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IsValidCredentialsListener {

    private final ISecurityService securityService;

    @Logged(logException = true)
    @Listener(queues = SecurityQueues.SECURITY_QUEUE)
    public boolean listen(IsValidCredentialsMessage message) {
        return securityService.isValid(message.username(), message.password());
    }

}
