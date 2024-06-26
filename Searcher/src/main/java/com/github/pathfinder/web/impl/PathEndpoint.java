package com.github.pathfinder.web.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.service.impl.PathSearcher;
import com.github.pathfinder.web.IPathEndpoint;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import com.github.pathfinder.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PathEndpoint implements IPathEndpoint {

    private final PathSearcher pathSearcher;

    @Override
    @Logged("request")
    public FoundPathDto find(@Valid @RequestBody FindPathDto request) {
        var mapper = DtoMapper.INSTANCE;

        return mapper.foundPathDto(pathSearcher.aStar(mapper.findPathRequest(request)));
    }

}
