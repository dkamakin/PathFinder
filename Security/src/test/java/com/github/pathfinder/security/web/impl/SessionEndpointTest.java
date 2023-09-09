package com.github.pathfinder.security.web.impl;

import com.github.pathfinder.core.tools.impl.JsonTools;
import com.github.pathfinder.security.SecurityFixtures;
import com.github.pathfinder.security.configuration.SecurityWebMvcTest;
import com.github.pathfinder.security.data.user.UserConstant;
import com.github.pathfinder.security.service.IAuthenticationService;
import com.github.pathfinder.security.web.dto.AuthenticationRequestDto;
import com.github.pathfinder.security.web.dto.DeviceInfoDto;
import com.github.pathfinder.security.web.dto.SessionRefreshRequestDto;
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
import static com.github.pathfinder.security.SecurityFixtures.string;
import static com.github.pathfinder.security.data.user.UserConstant.DEVICE_NAME_MAX_LENGTH;
import static com.github.pathfinder.security.data.user.UserConstant.PASSWORD_MAX_LENGTH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SecurityWebMvcTest
@Import(SessionEndpoint.class)
class SessionEndpointTest {

    static class Data {
        static final DeviceInfoDto DEVICE = new DeviceInfoDto(SecurityFixtures.DEVICE_NAME);
    }

    @Autowired
    JsonTools jsonTools;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IAuthenticationService authenticationService;

    static Stream<AuthenticationRequestDto> authenticationBadRequests() {
        return Stream.of(
                new AuthenticationRequestDto(null, SecurityFixtures.PASSWORD, Data.DEVICE),
                new AuthenticationRequestDto("", SecurityFixtures.PASSWORD, Data.DEVICE),
                new AuthenticationRequestDto("    ", SecurityFixtures.PASSWORD, Data.DEVICE),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, null, Data.DEVICE),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, "", Data.DEVICE),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, "    ", Data.DEVICE),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, SecurityFixtures.PASSWORD, null),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, SecurityFixtures.PASSWORD,
                                             new DeviceInfoDto(null)),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, SecurityFixtures.PASSWORD,
                                             new DeviceInfoDto("")),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, SecurityFixtures.PASSWORD,
                                             new DeviceInfoDto("   ")),
                new AuthenticationRequestDto(string(UserConstant.NAME_MAX_LENGTH + 1), SecurityFixtures.PASSWORD,
                                             Data.DEVICE),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, "a", Data.DEVICE),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, string(PASSWORD_MAX_LENGTH + 1), Data.DEVICE),
                new AuthenticationRequestDto(SecurityFixtures.USERNAME, SecurityFixtures.PASSWORD,
                                             new DeviceInfoDto(string(DEVICE_NAME_MAX_LENGTH + 1)))
        );
    }

    static Stream<SessionRefreshRequestDto> refreshBadRequests() {
        return Stream.of(
                new SessionRefreshRequestDto(null),
                new SessionRefreshRequestDto(""),
                new SessionRefreshRequestDto("    ")
        );
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("authenticationBadRequests")
    void authenticate_BadRequest_BadRequestStatus(AuthenticationRequestDto request) throws Exception {
        mockMvc.perform(post("/user/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTools.serialize(request))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    @WithMockUser
    void authenticate_ValidRequest_CallService() throws Exception {
        var request = new AuthenticationRequestDto(SecurityFixtures.USERNAME, SecurityFixtures.PASSWORD, Data.DEVICE);

        mockMvc.perform(post("/user/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTools.serialize(request))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk());

        verify(authenticationService).authenticate(any());
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("refreshBadRequests")
    void refresh_BadRequest_BadRequestStatus(SessionRefreshRequestDto request) throws Exception {
        mockMvc.perform(put("/user/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTools.serialize(request))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void refresh_ValidRequest_CallService() throws Exception {
        var request = new SessionRefreshRequestDto(SecurityFixtures.randomString());

        mockMvc.perform(put("/user/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonTools.serialize(request))
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk());

        verify(authenticationService).refresh(any());
    }
}
