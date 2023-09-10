package com.github.pathfinder.core.web.configuration;

import io.swagger.v3.oas.models.Operation;
import java.util.Set;
import org.springframework.web.method.HandlerMethod;

public interface IOperationCustomizer {

    boolean isMatch(Operation operation, HandlerMethod handlerMethod, Set<String> tags);

    void customize(Operation operation, HandlerMethod handlerMethod, Set<String> tags);

}
