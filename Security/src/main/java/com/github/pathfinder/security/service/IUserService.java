package com.github.pathfinder.security.service;

import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.provisioning.UserDetailsManager;

public interface IUserService extends UserDetailsManager, UserDetailsPasswordService {

}
