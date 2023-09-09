package com.github.pathfinder.security.data;

import com.github.pathfinder.security.api.data.UserInfo;
import com.github.pathfinder.security.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InternalMapper {

    InternalMapper INSTANCE = Mappers.getMapper(InternalMapper.class);

    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "role", source = "user.roles.role")
    UserInfo userInfo(UserEntity user);

}
