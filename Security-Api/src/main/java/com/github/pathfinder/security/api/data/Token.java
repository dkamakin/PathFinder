package com.github.pathfinder.security.api.data;

import com.google.common.base.MoreObjects;

public record Token(String value) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
