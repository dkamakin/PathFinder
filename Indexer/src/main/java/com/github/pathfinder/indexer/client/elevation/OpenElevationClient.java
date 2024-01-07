package com.github.pathfinder.indexer.client.elevation;

import com.github.pathfinder.core.tools.impl.JsonTools;
import com.github.pathfinder.indexer.configuration.elevation.OpenElevationConfiguration;
import com.github.pathfinder.indexer.data.elevation.Elevation;
import com.github.pathfinder.indexer.data.elevation.ElevationCoordinate;
import com.github.pathfinder.indexer.data.elevation.ElevationMapper;
import com.github.pathfinder.indexer.data.elevation.OpenElevationRequest;
import com.github.pathfinder.indexer.data.elevation.OpenElevationResponse;
import com.google.common.collect.Lists;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenElevationClient implements ElevationClient {

    private final OpenElevationConfiguration configuration;
    private final JsonTools                  jsonTools;
    private final HttpClient                 httpClient;

    @Override
    public List<Elevation> elevations(List<ElevationCoordinate> coordinates) {
        return Lists.partition(coordinates, configuration.getBatchSize()).stream()
                .map(this::elevationsInternal)
                .flatMap(Collection::stream)
                .toList();
    }

    @SneakyThrows
    private List<Elevation> elevationsInternal(List<ElevationCoordinate> coordinates) {
        var request = request(coordinates);
        var result  = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return jsonTools.deserialize(result.body(), OpenElevationResponse.class).results().stream()
                .map(ElevationMapper.MAPPER::elevation)
                .toList();
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
