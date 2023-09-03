package com.github.pathfinder.web.impl;

import com.github.pathfinder.configuration.SearcherWebMvcTest;
import com.github.pathfinder.core.tools.impl.JsonTools;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.data.path.FindPathResponse;
import com.github.pathfinder.security.api.role.SecurityRoles;
import com.github.pathfinder.service.IPathService;
import com.github.pathfinder.web.dto.CoordinateDto;
import com.github.pathfinder.web.dto.HealthTypeDto;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import com.github.pathfinder.web.mapper.DtoMapper;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SearcherWebMvcTest
@Import(PathEndpoint.class)
class PathEndpointTest {

    @Autowired
    JsonTools jsonTools;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IPathService pathService;

    static Stream<FindPathDto> validFindPaths() {
        return Stream.of(
                new FindPathDto(
                        new CoordinateDto(1, 2),
                        new CoordinateDto(2, 3),
                        HealthTypeDto.HEALTHY),
                new FindPathDto(
                        new CoordinateDto(3, 19),
                        new CoordinateDto(0, 15),
                        HealthTypeDto.WEAKENED),
                new FindPathDto(
                        new CoordinateDto(10, 22),
                        new CoordinateDto(20, 31),
                        null),
                new FindPathDto(
                        new CoordinateDto(10, 22),
                        new CoordinateDto(20, 31),
                        HealthTypeDto.WOUNDED)
        );
    }

    static Stream<FindPathDto> invalidFindPaths() {
        return Stream.of(
                new FindPathDto(
                        new CoordinateDto(null, 2),
                        new CoordinateDto(2, 3),
                        HealthTypeDto.HEALTHY),
                new FindPathDto(
                        new CoordinateDto(1, null),
                        new CoordinateDto(2, 3),
                        HealthTypeDto.WOUNDED),
                new FindPathDto(
                        null,
                        new CoordinateDto(2, 3),
                        HealthTypeDto.WEAKENED)
        );
    }

    void whenNeedToReturn(FindPathRequest request, FindPathResponse response) {
        when(pathService.find(request)).thenReturn(response);
    }

    @ParameterizedTest
    @MethodSource("validFindPaths")
    @WithMockUser(roles = SecurityRoles.PATH_SEARCHER)
    void find_ValidRequest_PassToService(FindPathDto request) throws Exception {
        var response = new FindPathResponse(1);
        var expected = DtoMapper.map(response);

        whenNeedToReturn(DtoMapper.map(request), response);

        var actual = jsonTools.deserialize(mockMvc.perform(post("/path")
                                                                   .contentType(MediaType.APPLICATION_JSON)
                                                                   .content(jsonTools.serialize(request))
                                                                   .with(SecurityMockMvcRequestPostProcessors.csrf())
                                                   )
                                                   .andExpect(status().isOk())
                                                   .andReturn()
                                                   .getResponse()
                                                   .getContentAsString(),
                                           FoundPathDto.class);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("invalidFindPaths")
    @WithMockUser(roles = SecurityRoles.PATH_SEARCHER)
    void find_InvalidRequests_CorrectError(FindPathDto request) throws Exception {
        mockMvc.perform(post("/path")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTools.serialize(request))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }

}
