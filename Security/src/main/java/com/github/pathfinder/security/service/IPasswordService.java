package com.github.pathfinder.security.service;

public interface IPasswordService {

    boolean matches(String rawPassword, String userPassword);

    String encode(String rawPassword);

}
