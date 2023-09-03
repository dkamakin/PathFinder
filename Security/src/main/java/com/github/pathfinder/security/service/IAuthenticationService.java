package com.github.pathfinder.security.service;

import com.github.pathfinder.security.api.data.AuthenticationRequest;
import com.github.pathfinder.security.api.data.AuthenticationResponse;

public interface IAuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
