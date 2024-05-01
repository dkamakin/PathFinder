package com.github.pathfinder.indexer.data.osm;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.pathfinder.indexer.configuration.IndexerOsmTestConfiguration;
import com.github.pathfinder.indexer.configuration.OsmElementFixtures;
import com.github.pathfinder.indexer.configuration.OsmTestTemplate;
import com.github.pathfinder.indexer.service.osm.impl.OsmLandTypeExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@IndexerOsmTestConfiguration
@Import(OsmLandTypeExtractor.class)
class OsmLandTypeExtractorTest {

    @Autowired
    OsmLandTypeExtractor target;

    @Autowired
    OsmTestTemplate osmTestTemplate;

    @Test
    void hasLandType_NoLandType_False() {
        var node = OsmElementFixtures.randomNode(1L, Map.of());

        assertThat(target.hasLandType(node)).isFalse();
    }

    @Test
    void hasLandType_ValueNotSupported_False() {
        var node = OsmElementFixtures.randomNode(1L, Map.of(osmTestTemplate.supportedKey(), "test"));

        assertThat(target.hasLandType(node)).isFalse();
    }

    @Test
    void hasLandType_LandTypePresent_True() {
        var value = osmTestTemplate.supportedLandType();
        var node  = OsmElementFixtures.randomNode(1L, Map.of(osmTestTemplate.supportedKey(), value.name()));

        assertThat(target.hasLandType(node)).isTrue();
    }

    @Test
    void from_SupportedTagsNotFound_EmptyResult() {
        var tags = new OsmTags(Map.of("random", "grass"));

        var actual = target.from(tags);

        assertThat(actual).isEmpty();
    }

    @Test
    void from_HasMultipleSupportedKeys_FindFirst() {
        var expected = osmTestTemplate.supportedLandType();
        var tags = new OsmTags(Map.of(osmTestTemplate.supportedKey(0), expected.name(),
                                      osmTestTemplate.supportedKey(1), "ssand"));

        var actual = target.from(tags);

        assertThat(actual).contains(expected);
    }

}
