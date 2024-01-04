package com.github.pathfinder.security.api.data;

import jakarta.validation.Valid;

public record GetUserInfoMessage(@Valid Token token) {

}
