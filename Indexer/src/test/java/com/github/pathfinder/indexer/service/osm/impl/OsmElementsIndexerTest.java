package com.github.pathfinder.indexer.service.osm.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.IndexerOsmTestConfiguration;
import com.github.pathfinder.indexer.configuration.OsmElementFixtures;
import com.github.pathfinder.indexer.configuration.OsmTestTemplate;
import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmTags;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@IndexerOsmTestConfiguration
@Import({OsmElementsIndexer.class, OsmLandTypeExtractor.class})
class OsmElementsIndexerTest {

    @MockBean
    OsmClient osmClient;

    @Autowired
    OsmElementsIndexer target;

    @Autowired
    OsmTestTemplate osmTestTemplate;

    void whenNeedToGetNodes(List<Long> ids, List<OsmNode> expected) {
        when(osmClient.nodes(ids)).thenReturn(expected);
    }

    @Test
    void index_ContainsOnlyNodes_ReturnNodes() {
        var key      = osmTestTemplate.supportedKey();
        var node     = OsmElementFixtures.randomNode(1, Map.of(key, osmTestTemplate.supportedLandType().name()));
        var elements = Stream.of(node).map(OsmElement.class::cast).toList();
        var actual   = target.index(elements);

        assertThat(actual)
                .satisfies(x -> assertThat(actual.nodes()).isEqualTo(elements));
    }

    @Test
    void index_NodeHasNoLandType_FilterNode() {
        var key      = "random";
        var node     = OsmElementFixtures.randomNode(1, Map.of(key, "test"));
        var elements = Stream.of(node).map(OsmElement.class::cast).toList();
        var actual   = target.index(elements);

        assertThat(actual)
                .matches(x -> x.nodes().isEmpty());
    }

    @Test
    void index_WayHasNoLandType_FilterNode() {
        var key      = osmTestTemplate.supportedKey();
        var way      = new OsmWay(List.of(1L), new OsmTags(Map.of(key, osmTestTemplate.supportedLandType().name())));
        var elements = Stream.of(way).map(OsmElement.class::cast).toList();
        var actual   = target.index(elements);

        assertThat(actual)
                .matches(x -> x.nodes().isEmpty());
    }

    @Test
    void index_ElementsContainAWay_ExtractNodesFromAWay() {
        var nodeId   = 1L;
        var tags     = Map.of(osmTestTemplate.supportedKey(), osmTestTemplate.supportedLandType().name());
        var way      = new OsmWay(List.of(nodeId), new OsmTags(tags));
        var node     = OsmElementFixtures.randomNode(nodeId, tags);
        var elements = Stream.of(way).map(OsmElement.class::cast).toList();

        whenNeedToGetNodes(way.nodeIds(), List.of(node));

        var actual = target.index(elements);

        assertThat(actual)
                .satisfies(x -> assertThat(x.nodes()).hasSize(1).contains(node))
                .satisfies(x -> assertThat(x.reverseWayIndex())
                        .hasSize(1)
                        .contains(Map.entry(node.id(), List.of(way))));
    }

    @Test
    void index_ElementsContainMultipleWays_ExtractNodesFromWays() {
        var firstNodeId  = 1L;
        var secondNodeId = 2L;
        var tags         = Map.of(osmTestTemplate.supportedKey(), osmTestTemplate.supportedLandType().name());
        var firstWay     = new OsmWay(List.of(firstNodeId), new OsmTags(tags));
        var secondWay    = new OsmWay(List.of(firstNodeId, secondNodeId), new OsmTags(tags));
        var firstNode    = OsmElementFixtures.randomNode(firstNodeId, tags);
        var secondNode   = OsmElementFixtures.randomNode(secondNodeId, tags);
        var elements     = Stream.of(firstWay, secondWay).map(OsmElement.class::cast).toList();

        whenNeedToGetNodes(firstWay.nodeIds(), List.of(firstNode));
        whenNeedToGetNodes(secondWay.nodeIds(), List.of(firstNode, secondNode));

        var actual = target.index(elements);

        assertThat(actual)
                .satisfies(x -> assertThat(x.nodes()).hasSize(2).contains(firstNode, secondNode))
                .satisfies(x -> assertThat(x.reverseWayIndex())
                        .hasSize(2)
                        .contains(Map.entry(firstNodeId, List.of(firstWay, secondWay)))
                        .contains(Map.entry(secondNodeId, List.of(secondWay))));
    }

}