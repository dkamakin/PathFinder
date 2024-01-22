package com.github.pathfinder.indexer.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmExtendedBoxIndex;
import com.github.pathfinder.indexer.data.osm.OsmExtendedNode;
import com.github.pathfinder.indexer.data.osm.OsmLandType;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmTags;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import com.github.pathfinder.indexer.service.osm.impl.OsmLandTypeExtractor;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OsmLandTypeExtractorTest {

    static final String LAND_TYPE_TAG = "natural";

    OsmExtendedNode node(Map<String, String> tags) {
        return new OsmExtendedNode(null, new OsmNode(1, null, new OsmTags(tags)));
    }

    OsmExtendedBoxIndex index(Long id, List<OsmWay> ways) {
        return new OsmExtendedBoxIndex(List.of(), Map.of(id, ways));
    }

    OsmWay osmWay(Map<String, String> tags) {
        return new OsmWay(List.of(), new OsmTags(tags));
    }

    @Test
    void maxLandType_PointDoesNotBelongToWayAndDoesNotHaveALandType_EmptyResult() {
        var node  = node(Map.of());
        var index = index(node.node().id(), List.of());

        var actual = OsmLandTypeExtractor.maxLandType(node, index);

        assertThat(actual).isEmpty();
    }

    @Test
    void maxLandType_PointDoesNotBelongToWay_ExtractFromPoint() {
        var landType = OsmLandType.from("beach").get();
        var node     = node(Map.of(LAND_TYPE_TAG, landType.name()));
        var index    = index(node.node().id(), List.of());

        var actual = OsmLandTypeExtractor.maxLandType(node, index);

        assertThat(actual).contains(landType);
    }

    @Test
    void maxLandType_PointBelongsToWayAndDoesNotHaveATag_ExtractFromWay() {
        var landType = OsmLandType.from("beach").get();
        var node     = node(Map.of());
        var index    = index(node.node().id(), List.of(osmWay(Map.of(LAND_TYPE_TAG, landType.name()))));

        var actual = OsmLandTypeExtractor.maxLandType(node, index);

        assertThat(actual).contains(landType);
    }

    @Test
    void maxLandType_PointBelongsToWayButLandTypeIsNotPresent_EmptyResult() {
        var node  = node(Map.of());
        var index = index(node.node().id(), List.of(osmWay(Map.of())));

        var actual = OsmLandTypeExtractor.maxLandType(node, index);

        assertThat(actual).isEmpty();
    }

    @Test
    void maxLandType_PointBelongsToWayAndHasAMaxLandTypeInNode_ReturnMaxLandType() {
        var wayLandType  = OsmLandType.from("beach").get();
        var nodeLandType = OsmLandType.from("mud").get();
        var node         = node(Map.of(LAND_TYPE_TAG, nodeLandType.name()));
        var index        = index(node.node().id(), List.of(osmWay(Map.of(LAND_TYPE_TAG, wayLandType.name()))));

        var actual = OsmLandTypeExtractor.maxLandType(node, index);

        assertThat(actual).contains(nodeLandType);
    }

    @Test
    void maxLandType_PointBelongsToWayAndHasAMaxLandTypeInWay_ReturnMaxLandType() {
        var wayLandType  = OsmLandType.from("mud").get();
        var nodeLandType = OsmLandType.from("beach").get();
        var node         = node(Map.of(LAND_TYPE_TAG, nodeLandType.name()));
        var index        = index(node.node().id(), List.of(osmWay(Map.of(LAND_TYPE_TAG, wayLandType.name()))));

        var actual = OsmLandTypeExtractor.maxLandType(node, index);

        assertThat(actual).contains(wayLandType);
    }

    @Test
    void maxLandType_PointBelongsToMultipleWaysAndHasATah_ReturnMaxLandType() {
        var wayLandType  = OsmLandType.from("mud").get();
        var nodeLandType = OsmLandType.from("beach").get();
        var expected     = OsmLandType.from("crevasse").get();
        var node         = node(Map.of(LAND_TYPE_TAG, nodeLandType.name()));
        var index = index(node.node().id(),
                          List.of(
                                  osmWay(Map.of(LAND_TYPE_TAG, wayLandType.name())),
                                  osmWay(Map.of(LAND_TYPE_TAG, expected.name()))
                          ));

        var actual = OsmLandTypeExtractor.maxLandType(node, index);

        assertThat(actual).contains(expected);
    }

}
