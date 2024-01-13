package com.github.pathfinder.indexer.service.osm.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.data.osm.OsmBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmElementType;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsmElementsIndexer {

    private final OsmClient osmClient;

    @Logged(ignoreReturnValue = false)
    public OsmBoxIndex index(List<OsmElement> elements) {
        var elementsPartitionedByIsWay = preprocess(elements);
        var nodes                      = handleNodes(elementsPartitionedByIsWay.get(false));
        var reverseWayIndex            = reverseWayIndex(elementsPartitionedByIsWay.get(true));

        return new OsmBoxIndex(concatNodes(reverseWayIndex, nodes), reverseWayIndex);
    }

    private Map<Boolean, List<OsmElement>> preprocess(List<OsmElement> elements) {
        return elements.stream()
                .filter(OsmElementFilter::isSupported)
                .collect(Collectors.partitioningBy(this::isWay));
    }

    private Map<Long, List<OsmWay>> reverseWayIndex(List<OsmElement> ways) {
        return ways.stream()
                .map(OsmElement::asWay)
                .flatMap(way -> way.nodeIds().stream().map(node -> Map.entry(node, way)))
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toList())));
    }

    private Map<Long, OsmNode> handleNodes(List<OsmElement> nodes) {
        return nodes.stream().map(OsmElement::asNode).collect(Collectors.toMap(OsmNode::id, Function.identity()));
    }

    private boolean isWay(OsmElement element) {
        return element.type() == OsmElementType.WAY;
    }

    private List<OsmNode> concatNodes(Map<Long, List<OsmWay>> reverseWayIndex, Map<Long, OsmNode> nodes) {
        var wayNodesIds = reverseWayIndex.keySet().stream().filter(Predicate.not(nodes::containsKey)).toList();

        log.info("Got {} nodes ids from ways", wayNodesIds.size());

        var waysNodes = osmClient.nodes(wayNodesIds);

        log.info("Fetched {} new nodes from ways", waysNodes.size());

        return Stream.concat(waysNodes.stream(), nodes.values().stream()).toList();
    }

}
