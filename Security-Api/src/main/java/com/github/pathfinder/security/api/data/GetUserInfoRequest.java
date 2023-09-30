package com.github.pathfinder.security.api.data;

import jakarta.validation.Valid;

public record GetUserInfoRequest(@Valid Token token) {

}
