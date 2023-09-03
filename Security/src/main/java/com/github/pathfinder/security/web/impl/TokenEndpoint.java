package com.github.pathfinder.security.web.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.api.role.SecurityRoles;
import com.github.pathfinder.security.web.ITokenEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenEndpoint implements ITokenEndpoint {

    @Logged
    @Override
    @GetMapping
    @Secured(SecurityRoles.PATH_SEARCHER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void isValid() {
        // Token is valid since it was checked by the security filter
    }
}
