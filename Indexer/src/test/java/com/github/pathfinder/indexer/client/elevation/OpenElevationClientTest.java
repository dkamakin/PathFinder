package com.github.pathfinder.indexer.client.elevation;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.github.pathfinder.indexer.configuration.IndexerOpenElevationTestConfiguration;
import com.github.pathfinder.indexer.data.elevation.ElevationCoordinate;
import com.github.pathfinder.indexer.exception.ApiExecutionException;
import lombok.SneakyThrows;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import(OpenElevationClient.class)
@IndexerOpenElevationTestConfiguration
class OpenElevationClientTest {

    @MockBean
    HttpClient httpClient;

    @Autowired
    ElevationClient target;

    @SneakyThrows
    void whenNeedToGetResponse(HttpResponse<String> expected) {
        doReturn(expected).when(httpClient).send(any(), any());
    }

    HttpResponse<String> response(int statusCode) {
        var response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(statusCode);

        return response;
    }

    @Test
    @SneakyThrows
    void nodes_ClientError_NoRetry() {
        whenNeedToGetResponse(response(HttpStatus.SC_CLIENT_ERROR));

        var request = List.of(new ElevationCoordinate(1D, 1D));

        assertThatThrownBy(() -> target.elevations(request)).isInstanceOf(IllegalArgumentException.class);

        verify(httpClient).send(any(), any());
    }

    @Test
    @SneakyThrows
    void nodes_ExceptionOccurred_RetryException() {
        whenNeedToGetResponse(response(HttpStatus.SC_INTERNAL_SERVER_ERROR));

        var request = List.of(new ElevationCoordinate(1D, 1D));

        assertThatThrownBy(() -> target.elevations(request)).isInstanceOf(ApiExecutionException.class);

        verify(httpClient, times(5)).send(any(), any());
    }

}