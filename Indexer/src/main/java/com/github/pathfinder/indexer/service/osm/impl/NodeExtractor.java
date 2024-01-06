package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NodeExtractor {

    private final OsmClient osmClient;

    public List<OsmNode> nodes(List<OsmElement> elements) {
        return nodes(elements.stream().collect(Collectors.partitioningBy(this::isWay)));
    }

    private List<OsmNode> nodes(Map<Boolean, List<OsmElement>> partitionedElementsByIsWay) {
        var nodes = extractNodes(partitionedElementsByIsWay);

        return Stream.concat(
                nodes.values().stream(),
                handleWays(partitionedElementsByIsWay.get(true), nodes).stream()
        ).toList();
    }

    private Map<Long, OsmNode> extractNodes(Map<Boolean, List<OsmElement>> partitionedElementsByIsWay) {
        return partitionedElementsByIsWay.get(false).stream()
                .map(OsmElement::asNode)
                .collect(Collectors.toMap(OsmNode::id, Function.identity()));
    }

    private boolean isWay(OsmElement element) {
        return element.type() == OsmElementType.WAY;
    }

    private List<OsmNode> handleWays(List<OsmElement> ways, Map<Long, OsmNode> nodes) {
        return osmClient.nodes(ways.stream()
                                       .map(OsmElement::asWay)
                                       .map(OsmWay::nodeIds)
                                       .flatMap(Collection::stream)
                                       .filter(Predicate.not(nodes::containsKey))
                                       .toList());
    }

}
