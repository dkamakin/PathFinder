package com.github.pathfinder.exception;

import com.github.pathfinder.core.exception.InternalServerException;
import java.lang.reflect.Field;

public class AnnotationNotFoundException extends InternalServerException {

    public AnnotationNotFoundException(Class<?> annotation, Field field) {
        super("Annotation '%s' on a field '%s' not found", annotation, field);
    }

}
