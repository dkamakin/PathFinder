package com.github.pathfinder.web.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.security.api.role.SecurityRoles;
import com.github.pathfinder.service.IPathService;
import com.github.pathfinder.web.IPathEndpoint;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import com.github.pathfinder.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/path")
public class PathEndpoint implements IPathEndpoint {

    private final IPathService pathService;

    @Logged
    @Override
    @PostMapping
    @Secured(SecurityRoles.PATH_SEARCHER)
    public FoundPathDto find(@Valid @RequestBody FindPathDto request) {
        var mapper = DtoMapper.INSTANCE;

        return mapper.map(pathService.find(mapper.map(request)));
    }

}
