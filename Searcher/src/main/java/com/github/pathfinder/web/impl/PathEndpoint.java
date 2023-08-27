package com.github.pathfinder.web.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.api.roles.SecurityRoles;
import com.github.pathfinder.service.IPathService;
import com.github.pathfinder.web.IPathEndpoint;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import com.github.pathfinder.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PathEndpoint implements IPathEndpoint {

    private final IPathService pathService;

    @Override
    @Logged(arguments = {"request"})
    @Secured(SecurityRoles.PATH_SEARCH)
    public FoundPathDto find(@Valid @RequestBody FindPathDto request) {
        return DtoMapper.map(pathService.find(DtoMapper.map(request)));
    }

}
