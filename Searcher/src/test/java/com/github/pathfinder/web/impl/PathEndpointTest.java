package com.github.pathfinder.web.impl;

import com.github.pathfinder.configuration.SearcherWebMvcTest;
import com.github.pathfinder.core.tools.impl.JsonTools;
import com.github.pathfinder.data.path.AStarResult;
import com.github.pathfinder.data.path.FindPathRequest;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.security.api.role.SecurityRoles;
import com.github.pathfinder.service.IPathSearcher;
import com.github.pathfinder.web.dto.CoordinateDto;
import com.github.pathfinder.web.dto.path.FindPathDto;
import com.github.pathfinder.web.dto.path.FoundPathDto;
import com.github.pathfinder.web.mapper.DtoMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    static final FindPathDto   FIND_PATH_REQUEST = new FindPathDto(COORDINATE_SOURCE, COORDINATE_TARGET);

    @Autowired
    JsonTools jsonTools;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IPathSearcher pathSearcher;

    static Stream<FindPathDto> invalidFindPaths() {
        return Stream.of(
                new FindPathDto(
                        new CoordinateDto(null, LATITUDE),
                        new CoordinateDto(LONGITUDE, LATITUDE)),
                new FindPathDto(
                        new CoordinateDto(LONGITUDE, null),
                        new CoordinateDto(LONGITUDE, LATITUDE)),
                new FindPathDto(
                        new CoordinateDto(LONGITUDE, null),
                        null),
                new FindPathDto(
                        null,
                        new CoordinateDto(LONGITUDE, LATITUDE))
        );
    }

    void whenNeedToReturn(FindPathRequest request, AStarResult response) {
        when(pathSearcher.aStar(request)).thenReturn(response);
    }

    @Test
    @WithMockUser(roles = SecurityRoles.PATH_SEARCHER)
    void find_ValidRequest_PassToService() throws Exception {
        var request   = FIND_PATH_REQUEST;
        var longitude = 23D;
        var latitude  = 22.5D;
        var meters    = 100D;
        var expected = new FoundPathDto(
                List.of(new CoordinateDto(longitude, latitude)),
                meters
        );
        var response = new AStarResult(
                List.of(PointNode.builder().passabilityCoefficient(1D).location(latitude, longitude, 1D).build()),
                1D,
                meters
        );

        whenNeedToReturn(DtoMapper.INSTANCE.findPathRequest(request), response);

        var json = mockMvc.perform(post("/path")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(jsonTools.serialize(request))
                                           .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        var actual = jsonTools.deserialize(json, FoundPathDto.class);

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

        verifyNoInteractions(pathSearcher);
    }

}
