package com.github.pathfinder.searcher.api.exception;

import com.github.pathfinder.core.exception.InternalServerException;
import java.io.Serial;
import java.lang.reflect.Field;

public class AnnotationNotFoundException extends InternalServerException {

    @Serial
    private static final long serialVersionUID = 2866536702717285297L;

    public AnnotationNotFoundException(Class<?> annotation, Field field) {
        super("Annotation '%s' on a field '%s' not found", annotation, field);
    }

}
