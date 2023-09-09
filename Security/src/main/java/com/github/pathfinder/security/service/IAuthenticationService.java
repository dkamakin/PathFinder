package com.github.pathfinder.security.service;

import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.AuthenticationResponse;
import com.github.pathfinder.security.api.data.SessionRefreshRequest;

public interface IAuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refresh(SessionRefreshRequest request);

}
