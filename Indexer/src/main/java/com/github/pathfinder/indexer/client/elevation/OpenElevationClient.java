package com.github.pathfinder.indexer.client.elevation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.impl.JsonTools;
import com.github.pathfinder.indexer.configuration.elevation.OpenElevationConfiguration;
import com.github.pathfinder.indexer.data.elevation.Elevation;
import com.github.pathfinder.indexer.data.elevation.ElevationCoordinate;
import com.github.pathfinder.indexer.data.elevation.ElevationMapper;
import com.github.pathfinder.indexer.data.elevation.OpenElevationRequest;
import com.github.pathfinder.indexer.data.elevation.OpenElevationResponse;
import com.github.pathfinder.indexer.exception.ApiExecutionException;
import com.github.pathfinder.indexer.tools.HttpTools;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ElevationClientRetryable
@RequiredArgsConstructor
public class OpenElevationClient implements ElevationClient {

    private final OpenElevationConfiguration configuration;
    private final JsonTools                  jsonTools;
    private final HttpClient                 httpClient;

    @Logged
    @Override
    public List<Elevation> elevations(List<ElevationCoordinate> coordinates) {
        return Lists.partition(coordinates, configuration.getBatchSize()).stream()
                .map(this::elevationsInternal)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Elevation> elevationsInternal(List<ElevationCoordinate> coordinates) {
        var request = request(coordinates);
        var result  = send(request);

        return jsonTools.deserialize(result.body(), OpenElevationResponse.class).results().stream()
                .map(ElevationMapper.MAPPER::elevation)
                .toList();
    }

    @SneakyThrows
    private HttpResponse<String> send(HttpRequest request) {
        var result = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (HttpTools.isServerError(result.statusCode())) {
            log.error("Failed to get elevations: {}", result.body());
            throw new ApiExecutionException("Failed to execute an API action");
        }

        return result;
    }

    private HttpRequest request(List<ElevationCoordinate> coordinates) {
        return HttpRequest.newBuilder()
                .uri(URI.create(configuration.getUri()))
                .POST(HttpRequest.BodyPublishers.ofString(body(coordinates), StandardCharsets.UTF_8))
                .build();
    }

    private String body(List<ElevationCoordinate> coordinates) {
        return jsonTools.serialize(new OpenElevationRequest(coordinates));
    }

}
