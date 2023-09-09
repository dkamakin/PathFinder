package com.github.pathfinder.core.web.exception.tools;

import com.github.pathfinder.core.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExceptionHandlerTools {

    public ErrorMessage message(Throwable throwable) {
        log.error("Exception caught", throwable);
        return new ErrorMessage(throwable.getMessage());
    }

}
