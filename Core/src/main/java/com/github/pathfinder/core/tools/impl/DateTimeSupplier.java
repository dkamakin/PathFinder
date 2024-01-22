package com.github.pathfinder.core.tools.impl;

import com.github.pathfinder.core.tools.IDateTimeSupplier;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class DateTimeSupplier implements IDateTimeSupplier {

    public Instant now() {
        return Instant.now();
    }

}
