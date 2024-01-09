package com.github.pathfinder.web;

import com.github.pathfinder.core.web.annotation.SwaggerDescriptionNonVoidMethod;
import com.github.pathfinder.security.api.role.SecurityRoles;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/path")
public interface IPathEndpoint {

    @PostMapping
    @SwaggerDescriptionNonVoidMethod(
            description = "Find terrain path",
            content = {@Content(schema = @Schema(implementation = FoundPathDto.class))}
    )
    @Secured(SecurityRoles.PATH_SEARCHER)
    @Operation(description = "Find terrain path")
    FoundPathDto find(FindPathDto request);

}
