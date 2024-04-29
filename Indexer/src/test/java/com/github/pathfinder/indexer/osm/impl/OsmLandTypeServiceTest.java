package com.github.pathfinder.indexer.osm.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.pathfinder.indexer.configuration.IndexerOsmTestConfiguration;
import com.github.pathfinder.indexer.configuration.OsmElementFixtures;
import com.github.pathfinder.indexer.configuration.OsmTestTemplate;
import com.github.pathfinder.indexer.data.osm.OsmExtendedBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.OsmTags;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import com.github.pathfinder.indexer.service.osm.impl.OsmLandTypeExtractor;
import com.github.pathfinder.indexer.service.osm.impl.OsmLandTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@IndexerOsmTestConfiguration
@Import({OsmLandTypeService.class, OsmLandTypeExtractor.class})
class OsmLandTypeServiceTest {

    @Autowired
    OsmLandTypeExtractor landTypeExtractor;

    @Autowired
    OsmLandTypeService target;

    @Autowired
    OsmTestTemplate osmTestTemplate;

    OsmExtendedNode extendedNode(Map<String, String> tags) {
        return new OsmExtendedNode(null, OsmElementFixtures.randomNode(1, tags));
    }

    OsmExtendedBoxIndex index(Long id, List<OsmWay> ways) {
        return new OsmExtendedBoxIndex(List.of(), Map.of(id, ways));
    }

    OsmWay osmWay(Map<String, String> tags) {
        return new OsmWay(List.of(), new OsmTags(tags));
    }

    OsmLandType max(OsmLandType... landTypes) {
        return extract(Comparator.comparing(OsmLandType::weight), landTypes);
    }

    OsmLandType min(OsmLandType... landTypes) {
        return extract(Comparator.comparing(OsmLandType::weight).reversed(), landTypes);
    }

    OsmLandType extract(Comparator<OsmLandType> comparator, OsmLandType... landTypes) {
        return Arrays.stream(landTypes).max(comparator).orElseThrow();
    }

    @Test
    void landTypeWithMaxWeight_PointDoesNotBelongToWayAndDoesNotHaveALandType_EmptyResult() {
        var node  = extendedNode(Map.of());
        var index = index(node.node().id(), List.of());

        var actual = target.landTypeWithMaxWeight(node, index);

        assertThat(actual).isEmpty();
    }

    @Test
    void landTypeWithMaxWeight_PointDoesNotBelongToWay_ExtractFromPoint() {
        var landType = osmTestTemplate.supportedLandType();
        var key      = osmTestTemplate.supportedKey();
        var node     = extendedNode(Map.of(key, landType.name()));
        var index    = index(node.node().id(), List.of());

        var actual = target.landTypeWithMaxWeight(node, index);

        assertThat(actual).contains(landType);
    }

    @Test
    void landTypeWithMaxWeight_PointBelongsToWayAndDoesNotHaveATag_ExtractFromWay() {
        var landType = osmTestTemplate.supportedLandType();
        var node     = extendedNode(Map.of());
        var key      = osmTestTemplate.supportedKey();
        var index    = index(node.node().id(), List.of(osmWay(Map.of(key, landType.name()))));

        var actual = target.landTypeWithMaxWeight(node, index);

        assertThat(actual).contains(landType);
    }

    @Test
    void landTypeWithMaxWeight_PointBelongsToWayButLandTypeIsNotPresent_EmptyResult() {
        var node  = extendedNode(Map.of());
        var index = index(node.node().id(), List.of(osmWay(Map.of())));

        var actual = target.landTypeWithMaxWeight(node, index);

        assertThat(actual).isEmpty();
    }

    @Test
    void landTypeWithMaxWeight_PointBelongsToWayAndHasAMaxLandTypeInNode_ReturnMaxLandType() {
        var first        = osmTestTemplate.supportedLandType(0);
        var second       = osmTestTemplate.supportedLandType(1);
        var wayLandType  = min(first, second);
        var nodeLandType = max(first, second);
        var key          = osmTestTemplate.supportedKey();
        var node         = extendedNode(Map.of(key, nodeLandType.name()));
        var index        = index(node.node().id(), List.of(osmWay(Map.of(key, wayLandType.name()))));

        var actual = target.landTypeWithMaxWeight(node, index);

        assertThat(actual).contains(nodeLandType);
    }

    @Test
    void landTypeWithMaxWeight_PointBelongsToWayAndHasAMaxLandTypeInWay_ReturnMaxLandType() {
        var wayLandType  = osmTestTemplate.supportedLandType(0);
        var nodeLandType = osmTestTemplate.supportedLandType(1);
        var key          = osmTestTemplate.supportedKey();
        var node         = extendedNode(Map.of(key, nodeLandType.name()));
        var index        = index(node.node().id(), List.of(osmWay(Map.of(key, wayLandType.name()))));

        var actual = target.landTypeWithMaxWeight(node, index);

        assertThat(actual).contains(wayLandType);
    }

    @Test
    void landTypeWithMaxWeight_PointBelongsToMultipleWaysAndHasATag_ReturnMaxLandType() {
        var wayLandType  = osmTestTemplate.supportedLandType(0);
        var nodeLandType = osmTestTemplate.supportedLandType(1);
        var another      = osmTestTemplate.supportedLandType(2);
        var expected     = max(wayLandType, nodeLandType, another);
        var key          = osmTestTemplate.supportedKey();
        var node         = extendedNode(Map.of(key, nodeLandType.name()));
        var index = index(node.node().id(),
                          List.of(
                                  osmWay(Map.of(key, wayLandType.name())),
                                  osmWay(Map.of(key, another.name()))
                          ));

        var actual = target.landTypeWithMaxWeight(node, index);

        assertThat(actual).contains(expected);
    }

}
