package com.github.pathfinder.security.api.data;

import com.google.common.base.MoreObjects;
import jakarta.validation.constraints.NotBlank;

public record Token(@NotBlank String value) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
