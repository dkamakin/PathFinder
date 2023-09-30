package com.github.pathfinder.security.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.security.api.data.GetUserInfoRequest;
import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.api.exception.InvalidTokenException;
import com.github.pathfinder.security.data.InternalMapper;
import com.github.pathfinder.security.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SecurityAmqpListener
@RequiredArgsConstructor
public class AuthenticationListener {

    private final IUserService userService;

    @AmqpHandler
    @Logged("request")
    public UserInfo handle(@Valid GetUserInfoRequest request) {
        return userService
                .read(request.token())
                .map(InternalMapper.INSTANCE::userInfo)
                .orElseThrow(InvalidTokenException::new);
    }

}
