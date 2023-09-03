package com.github.pathfinder.security.data.user;

public interface User {

    String username();

    String password();

    UserRoles roles();

}
