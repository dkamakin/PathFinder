package com.github.pathfinder.core.web.configuration;

import io.swagger.v3.oas.models.Operation;
import java.util.Set;
import org.springframework.web.method.HandlerMethod;

public interface IOperationTagCustomizer {

    boolean isMatch(Set<String> tags);

    void customize(Operation operation, HandlerMethod handlerMethod, Set<String> tags);

}
