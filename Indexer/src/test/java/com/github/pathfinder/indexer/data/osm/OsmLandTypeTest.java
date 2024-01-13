package com.github.pathfinder.indexer.data.osm;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

class OsmLandTypeTest {

    static Stream<String> supportedKeys() {
        return Stream.of(
                "natural",
                "surface",
                "landcover",
                "waterway"
        );
    }

    static Stream<Arguments> compareArguments() {
        return Stream.of(
                Arguments.of(-1D, 1D, 1),
                Arguments.of(1D, 1D, 0),
                Arguments.of(10D, 1D, 1),
                Arguments.of(-1D, 100D, 1),
                Arguments.of(1D, 10D, -1),
                Arguments.of(100D, -1D, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("compareArguments")
    void comparator_DifferentCases_ValidComparing(double first, double second, int expected) {
        var actual = OsmLandType.comparator().compare(new OsmLandType("first", first),
                                                      new OsmLandType("second", second));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void from_SupportedTagsNotFound_EmptyResult() {
        var tags = new OsmTags(Map.of("random", "grass"));

        var actual = OsmLandType.from(tags);

        assertThat(actual).isEmpty();
    }

    @Test
    void from_HasMultipleSupportedKeys_FindFirst() {
        var expected = new OsmLandType("sand", 3D);
        var tags = new OsmTags(Map.of("natural", expected.name(),
                                      "surface", "ssand"));

        var actual = OsmLandType.from(tags);

        assertThat(actual).contains(expected);
    }

    @ParameterizedTest
    @MethodSource("supportedKeys")
    void from_SurfaceTagIsPresent_FindValue(String key) {
        var expected = new OsmLandType("sand", 3D);
        var tags     = new OsmTags(Map.of(key, expected.name()));

        var actual = OsmLandType.from(tags);

        assertThat(actual).contains(expected);
    }

    @Test
    void from_TypeIsFound_ReturnLandType() {
        var expected = new OsmLandType("water", -1D);

        assertThat(OsmLandType.from(expected.name())).contains(expected);
    }

    @Test
    void from_TypeIsNotFound_EmptyResult() {
        assertThat(OsmLandType.from("test")).isEmpty();
    }

}
