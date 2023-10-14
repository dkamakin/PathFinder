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
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static com.github.pathfinder.PointFixtures.LATITUDE;
import static com.github.pathfinder.PointFixtures.LONGITUDE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SearcherWebMvcTest
@Import(PathEndpoint.class)
class PathEndpointTest {

    static final CoordinateDto COORDINATE_SOURCE = new CoordinateDto(1D, 2D);
    static final CoordinateDto COORDINATE_TARGET = new CoordinateDto(3D, 4D);
    static final FindPathDto   FIND_PATH_REQUEST = new FindPathDto(COORDINATE_SOURCE, COORDINATE_TARGET,
                                                                   HealthTypeDto.HEALTHY);

    @Autowired
    JsonTools jsonTools;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IPathService pathService;

    static Stream<FindPathDto> invalidFindPaths() {
        return Stream.of(
                new FindPathDto(
                        new CoordinateDto(null, LATITUDE),
                        new CoordinateDto(LONGITUDE, LATITUDE),
                        HealthTypeDto.HEALTHY),
                new FindPathDto(
                        new CoordinateDto(LONGITUDE, null),
                        new CoordinateDto(LONGITUDE, LATITUDE),
                        HealthTypeDto.WOUNDED),
                new FindPathDto(
                        new CoordinateDto(LONGITUDE, null),
                        null,
                        HealthTypeDto.WOUNDED),
                new FindPathDto(
                        null,
                        new CoordinateDto(LONGITUDE, LATITUDE),
                        HealthTypeDto.WEAKENED)
        );
    }

    void whenNeedToReturn(FindPathRequest request, FindPathResponse response) {
        when(pathService.find(request)).thenReturn(response);
    }

    @Test
    @WithMockUser(roles = SecurityRoles.PATH_SEARCHER)
    void find_ValidRequest_PassToService() throws Exception {
        var request  = FIND_PATH_REQUEST;
        var expected = new FindPathResponse(1);

        whenNeedToReturn(DtoMapper.INSTANCE.map(request), expected);

        var response = mockMvc.perform(post("/path")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(jsonTools.serialize(request))
                                               .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        var actual = jsonTools.deserialize(response, FoundPathDto.class);

        assertThat(actual)
                .matches(result -> result.cost().equals(expected.cost()));
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

        verifyNoInteractions(pathService);
    }

}
