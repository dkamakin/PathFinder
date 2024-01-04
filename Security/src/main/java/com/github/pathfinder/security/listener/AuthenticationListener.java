package com.github.pathfinder.security.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.error.RethrowingToSenderErrorHandler;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.messaging.listener.AmqpListener;
import com.github.pathfinder.security.api.data.GetUserInfoMessage;
import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.data.InternalMapper;
import com.github.pathfinder.security.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static com.github.pathfinder.security.api.configuration.SecurityMessagingConfiguration.Token.DEFAULT_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@AmqpListener(errorHandler = RethrowingToSenderErrorHandler.NAME, queues = DEFAULT_QUEUE_NAME)
public class AuthenticationListener {

    private final IUserService userService;

    @AmqpHandler
    @Logged("request")
    public UserInfo handle(@Valid GetUserInfoMessage request) {
        return userService
                .read(request.token())
                .map(InternalMapper.INSTANCE::userInfo)
                .orElseThrow(InvalidTokenException::new);
    }

}
