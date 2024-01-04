package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.indexer.client.elevation.ElevationClient;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.elevation.Elevation;
import com.github.pathfinder.indexer.data.elevation.ElevationMapper;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import com.github.pathfinder.searcher.api.data.point.Point;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmPointExtractor {

    private final OsmClient       osmClient;
    private final ElevationClient elevationClient;

    public List<Point> points(List<OsmElement> elements) {
        var partitionedElements = elements.stream().collect(Collectors.partitioningBy(this::isWay));
        var nodes = Stream.concat(
                partitionedElements.get(false).stream().map(OsmElement::asNode),
                handleWays(partitionedElements.get(true).stream().map(OsmElement::asWay)).stream()
        ).toList();
        var elevations = elevations(nodes);

        return nodes.stream()
                .map(node -> createPointRequest(node, elevations))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private boolean isWay(OsmElement element) {
        return element.type() == OsmElementType.WAY;
    }

    private Map<Coordinate, Elevation> elevations(List<OsmNode> nodes) {
        return elevationClient.elevations(
                nodes.stream()
                        .map(OsmNode::coordinate)
                        .map(ElevationMapper.MAPPER::elevationCoordinate)
                        .toList()
        ).stream().collect(Collectors.toMap(this::coordinate, Function.identity()));
    }

    private Coordinate coordinate(Elevation elevation) {
        return ElevationMapper.MAPPER.coordinate(elevation.coordinate());
    }

    private List<OsmNode> handleWays(Stream<OsmWay> ways) {
        return osmClient.elements(ways.map(OsmWay::nodeIds).flatMap(Collection::stream).toList());
    }

    private Optional<Point> createPointRequest(OsmNode node, Map<Coordinate, Elevation> elevations) {
        return node.natural()
                .flatMap(OsmLandType::from)
                .flatMap(landType -> createPointRequest(node, landType, elevations));
    }

    private Optional<Point> createPointRequest(OsmNode node, OsmLandType landType,
                                               Map<Coordinate, Elevation> elevations) {
        var elevation = elevations.get(node.coordinate());

        if (elevation == null) {
            log.warn("An elevation for the node {} is not found in the map", node);
            return Optional.empty();
        }

        if (elevation.elevation() == 0D) {
            log.warn("An elevation for the node {} at the sea level", node);
            return Optional.empty();
        }

        return Optional.of(
                new Point(elevation.elevation(),
                          node.coordinate(),
                          landType.name(),
                          landType.coefficient())
        );
    }

}
