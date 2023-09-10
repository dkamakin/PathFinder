package com.github.pathfinder.core.web.configuration.impl;

import com.github.pathfinder.core.web.configuration.IOperationCustomizer;
import com.google.common.collect.ImmutableSet;
import io.swagger.v3.oas.models.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
@RequiredArgsConstructor
public class OperationCustomizerProcessor implements OperationCustomizer {

    private final List<IOperationCustomizer> customizers;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var tags = ImmutableSet.copyOf(operation.getTags());

        customizers
                .stream()
                .filter(customizer -> customizer.isMatch(operation, handlerMethod, tags))
                .forEach(customizer -> customizer.customize(operation, handlerMethod, tags));

        return operation;
    }
}
