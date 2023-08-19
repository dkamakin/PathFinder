package com.github.pathfinder.security.data;

import com.github.pathfinder.security.database.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public class Mapper {

    public static UserDetails map(UserEntity entity) {
        return new UserDto(entity.getName(), entity.getPassword());
    }

}
