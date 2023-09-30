package com.github.pathfinder.security;

import com.github.pathfinder.security.api.data.DeviceInfo;
import com.github.pathfinder.security.api.data.Token;
import com.github.pathfinder.security.api.data.Tokens;
import com.github.pathfinder.security.data.jwt.JwtPayload;
import com.github.pathfinder.security.data.user.SaveUserRequest;
import com.github.pathfinder.security.data.user.UserConstant;
import com.github.pathfinder.security.database.entity.UserEntity;
import com.github.pathfinder.security.database.entity.UserRolesEntity;
import java.util.UUID;

public class SecurityFixtures {

    public static final String          USERNAME          = string(UserConstant.NAME_MIN_LENGTH);
    public static final String          PASSWORD          = string(UserConstant.PASSWORD_MIN_LENGTH);
    public static final String          ROLE              = "role";
    public static final Long            USER_ID           = 2L;
    public static final String          DEVICE_NAME       = "windows12";
    public static final DeviceInfo      DEVICE_INFO       = new DeviceInfo(DEVICE_NAME);
    public static final JwtPayload      JWT_PAYLOAD       = new JwtPayload(USER_ID, DEVICE_INFO);
    public static final SaveUserRequest SAVE_USER_REQUEST = new SaveUserRequest(USERNAME, PASSWORD, ROLE);
    public static final Token           TOKEN             = new Token("test");
    public static final UserRolesEntity USER_ROLES        = new UserRolesEntity("role");
    public static final UserEntity      USER_ENTITY       = new UserEntity("user", "123asd2", USER_ROLES);

    public static String string(int size) {
        return "a".repeat(size);
    }

    public static Token token() {
        return new Token(randomString());
    }

    public static Tokens tokens() {
        return new Tokens(token(), token());
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
