package com.github.pathfinder.core.tools.impl;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionTools {

    public <T> T cast(Object object) {
        return (T) object;
    }

}
