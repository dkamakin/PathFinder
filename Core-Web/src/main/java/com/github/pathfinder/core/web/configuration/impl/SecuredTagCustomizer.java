package com.github.pathfinder.core.web.configuration.impl;


import com.github.pathfinder.core.web.configuration.IOperationTagCustomizer;
import com.github.pathfinder.core.web.tools.SwaggerTags;
import io.swagger.v3.oas.models.Operation;
import java.util.Collections;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Slf4j
@Component
public class SecuredTagCustomizer implements IOperationTagCustomizer {

    @Override
    public boolean isMatch(Set<String> tags) {
        return tags.contains(SwaggerTags.UNSECURED);
    }

    @Override
    public void customize(Operation operation, HandlerMethod handlerMethod, Set<String> tags) {
        operation.setSecurity(Collections.emptyList());
    }
}
