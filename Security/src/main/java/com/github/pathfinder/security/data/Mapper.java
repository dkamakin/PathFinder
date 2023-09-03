package com.github.pathfinder.security.data;

import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.data.user.User;
import com.github.pathfinder.security.data.user.UserInternal;
import com.github.pathfinder.security.data.user.UserRoles;
import com.github.pathfinder.security.data.user.UserRolesInternal;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRolesEntity;

public class Mapper {

    public static User map(UserEntity entity) {
        return new UserInternal(entity.getName(), entity.getPassword(), map(entity.getRoles()));
    }

    public static UserRoles map(UserRolesEntity entity) {
        return new UserRolesInternal(entity.getRole());
    }

    public static UserInfo userInfo(User user) {
        return new UserInfo(user.username(), user.roles().role());
    }

}
