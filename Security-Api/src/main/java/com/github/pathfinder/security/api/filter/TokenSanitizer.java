package com.github.pathfinder.security.api.filter;

import org.springframework.stereotype.Component;

@Component
public class TokenSanitizer {

    private static final int SPACE = ' ';

    public String sanitize(String token) {
        var spaceIndex = token.indexOf(SPACE);

        if (spaceIndex == -1) {
            return token;
        } else {
            return token.substring(spaceIndex + 1);
        }
    }

}
