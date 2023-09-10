package com.github.pathfinder.security.api.configuration;


import com.github.pathfinder.core.web.configuration.IOperationCustomizer;
import io.swagger.v3.oas.models.Operation;
import java.util.Collections;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Slf4j
@Component
public class SecuredOperationCustomizer implements IOperationCustomizer {

    @Override
    public boolean isMatch(Operation operation, HandlerMethod handlerMethod, Set<String> tags) {
        return handlerMethod.getMethodAnnotation(Secured.class) == null;
    }

    @Override
    public void customize(Operation operation, HandlerMethod handlerMethod, Set<String> tags) {
        operation.setSecurity(Collections.emptyList());
    }
}
