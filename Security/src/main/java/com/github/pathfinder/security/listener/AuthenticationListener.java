package com.github.pathfinder.security.listener;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.messaging.listener.AmqpHandler;
import com.github.pathfinder.security.api.data.GetUserInfoRequest;
import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.api.exception.UserNotFoundException;
import com.github.pathfinder.security.data.Mapper;
import com.github.pathfinder.security.service.IUserService;
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
    @Logged(logException = true)
    public UserInfo handle(GetUserInfoRequest request) {
        return Mapper.userInfo(userService.read(request.token()).orElseThrow(UserNotFoundException::new));
    }

}
