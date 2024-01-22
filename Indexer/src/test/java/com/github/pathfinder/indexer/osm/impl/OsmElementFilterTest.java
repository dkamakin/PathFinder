package com.github.pathfinder.indexer.osm.impl;

import com.github.pathfinder.indexer.data.osm.OsmElement;
import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmTags;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import com.github.pathfinder.indexer.service.osm.impl.OsmElementFilter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

class OsmElementFilterTest {

    static Stream<OsmElement> supportedElements() {
        return Stream.of(
                new OsmWay(List.of(), new OsmTags(Map.of())),
                new OsmWay(List.of(), new OsmTags(Map.of("natural", "beach"))),
                new OsmWay(List.of(), new OsmTags(Map.of("natural", "random"))),
                new OsmWay(List.of(), new OsmTags(Map.of("random", "random"))),
                new OsmNode(1, null, new OsmTags(Map.of())),
                new OsmNode(1, null, new OsmTags(Map.of("natural", "beach"))),
                new OsmNode(1, null, new OsmTags(Map.of("natural", "random"))),
                new OsmNode(1, null, new OsmTags(Map.of("random", "random")))
        );
    }

    static Stream<OsmElement> notSupportedElements() {
        return Stream.of(
                new OsmWay(List.of(), new OsmTags(Map.of("natural", "water"))),
                new OsmWay(List.of(), new OsmTags(Map.of("waterway", "stream")))
        );
    }

    OsmElementFilter target;

    @BeforeEach
    void setUp() {
        target = new OsmElementFilter();
    }

    @ParameterizedTest
    @MethodSource("notSupportedElements")
    void test_NotSupported_False(OsmElement element) {
        var actual = target.test(element);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("supportedElements")
    void isSupported_Supported_True(OsmElement element) {
        var actual = target.test(element);

        assertThat(actual).isTrue();
    }

}
