package com.github.pathfinder.security.data.user;

import com.google.common.base.MoreObjects;

public record SaveUserRequest(String username,
                              String password,
                              String role) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("role", role)
                .toString();
    }
}
