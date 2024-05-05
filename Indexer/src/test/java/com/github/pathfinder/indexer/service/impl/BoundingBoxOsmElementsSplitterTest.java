package com.github.pathfinder.indexer.service.impl;

import java.util.List;
import java.util.stream.Stream;
import static com.github.pathfinder.indexer.data.OsmMapper.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.data.MetersDistance;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.IndexerOsmTestConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties;
import com.github.pathfinder.indexer.service.osm.impl.BoundingBoxOsmElementsSplitter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@IndexerOsmTestConfiguration
class BoundingBoxOsmElementsSplitterTest {

    static Stream<Arguments> boundingBoxesWithResult() {
        return Stream.of(
                Arguments.of(
                        new BoundingBox(new Coordinate(45.104546, 19.593430),
                                        new Coordinate(45.314495, 19.963531)),
                        List.of(
                                new BoundingBox(new Coordinate(45.104546, 19.59343),
                                                new Coordinate(45.314495, 19.779115309570365)),

                                new BoundingBox(new Coordinate(45.10439505246473, 19.779115309570365),
                                                new Coordinate(45.208996096744514, 19.963531)),

                                new BoundingBox(new Coordinate(45.208996096744514, 19.779115309570365),
                                                new Coordinate(45.26129589739023, 19.826173882175503)),

                                new BoundingBox(new Coordinate(45.20898640205888, 19.826173882175503),
                                                new Coordinate(45.23469131213847, 19.87195955272544)),

                                new BoundingBox(new Coordinate(45.23469131213847, 19.826173882175503),
                                                new Coordinate(45.26129589739023, 19.8497034621468)),

                                new BoundingBox(new Coordinate(45.23468888843091, 19.8497034621468),
                                                new Coordinate(45.26129589739023, 19.87195955272544)),

                                new BoundingBox(new Coordinate(45.26129589739023, 19.779115309570365),
                                                new Coordinate(45.314495, 19.87195955272544)),

                                new BoundingBox(new Coordinate(45.20895835983457, 19.87195955272544),
                                                new Coordinate(45.314495, 19.963531))
                        )),
                Arguments.of(
                        new BoundingBox(new Coordinate(44.809, 20.382729),
                                        new Coordinate(44.832378, 20.433712)),
                        List.of(
                                new BoundingBox(new Coordinate(44.809, 20.382729),
                                                new Coordinate(44.832378, 20.40885254319064)),
                                new BoundingBox(new Coordinate(44.80899701226119, 20.40885254319064),
                                                new Coordinate(44.832378, 20.433712))
                        )),
                Arguments.of(
                        new BoundingBox(new Coordinate(39.610978, -0.446320),
                                        new Coordinate(39.739930, -0.197754)),
                        List.of(
                                new BoundingBox(new Coordinate(39.610978, -0.44632),
                                                new Coordinate(39.73993, -0.321454888308976)),
                                new BoundingBox(new Coordinate(39.61091090290156, -0.321454888308976),
                                                new Coordinate(39.67497047557791, -0.197754)),
                                new BoundingBox(new Coordinate(39.67497047557791, -0.321454888308976),
                                                new Coordinate(39.73993, -0.197754))
                        )
                ),
                Arguments.of(
                        new BoundingBox(new Coordinate(39.610978, -0.446320),
                                        new Coordinate(39.610978, -0.446320)),
                        List.of()
                )
        );
    }

    @Autowired
    OsmClient osmClient;

    @Autowired
    OsmConfigurationProperties osmConfigurationProperties;

    @ParameterizedTest
    @MethodSource("boundingBoxesWithResult")
    void split_DifferentBoxes_ValidSplitting(BoundingBox box, List<BoundingBox> expected) {
        var target = BoundingBoxOsmElementsSplitter.builder()
                .elementsLimit(5000)
                .additionalSpace(new MetersDistance(50))
                .osmClient(osmClient)
                .tags(MAPPER.osmQueryTags(osmConfigurationProperties.getTags()))
                .build();

        var actual = target.split(box);

        assertThat(actual).isEqualTo(expected);
    }

}