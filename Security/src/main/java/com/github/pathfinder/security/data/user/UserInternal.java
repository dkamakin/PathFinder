package com.github.pathfinder.security.data.user;

import com.google.common.base.MoreObjects;

public record UserInternal(String username,
                           String password,
                           UserRoles roles) implements User {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("role", roles)
                .add("password", "***")
                .toString();
    }

}
