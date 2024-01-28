package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.data.MetersDistance;
import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.osm.OsmClientConfiguration;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        OsmClientConfiguration.class,
        RefreshAutoConfiguration.class
})
class BoundingBoxOsmElementsSplitterTest {

    static Stream<Arguments> boundingBoxesWithResult() {
        return Stream.of(
                Arguments.of(
                        new BoundingBox(new Coordinate(45.104546, 19.593430),
                                        new Coordinate(45.314495, 19.963531)),
                        List.of(
                                new BoundingBox(new Coordinate(45.104546, 19.59343),
                                                new Coordinate(45.209071569124404, 19.779115309570365)),

                                new BoundingBox(new Coordinate(45.209071569124404, 19.59343),
                                                new Coordinate(45.314495, 19.686909052304806)),

                                new BoundingBox(new Coordinate(45.20903331440974, 19.686909052304806),
                                                new Coordinate(45.261314506051725, 19.733648632540255)),

                                new BoundingBox(new Coordinate(45.20902375071204, 19.733648632540255),
                                                new Coordinate(45.261314506051725, 19.779115309570365)),

                                new BoundingBox(new Coordinate(45.261314506051725, 19.686909052304806),
                                                new Coordinate(45.314495, 19.779115309570365)),

                                new BoundingBox(new Coordinate(45.10439505246473, 19.779115309570365),
                                                new Coordinate(45.208996096744514, 19.871958389601076)),

                                new BoundingBox(new Coordinate(45.104357315283984, 19.871958389601076),
                                                new Coordinate(45.208996096744514, 19.963531)),

                                new BoundingBox(new Coordinate(45.208996096744514, 19.779115309570365),
                                                new Coordinate(45.26129589739023, 19.826173882175503)),

                                new BoundingBox(new Coordinate(45.20898640205888, 19.826173882175503),
                                                new Coordinate(45.23469131213847, 19.87195955272544)),

                                new BoundingBox(new Coordinate(45.23469131213847, 19.826173882175503),
                                                new Coordinate(45.26129589739023, 19.87195955272544)),

                                new BoundingBox(new Coordinate(45.26129589739023, 19.779115309570365),
                                                new Coordinate(45.314495, 19.826174466228174)),

                                new BoundingBox(new Coordinate(45.261286202668664, 19.826174466228174),
                                                new Coordinate(45.314495, 19.87195955272544)),

                                new BoundingBox(new Coordinate(45.20895835983457, 19.87195955272544),
                                                new Coordinate(45.314495, 19.963531))
                        )),
                Arguments.of(
                        new BoundingBox(new Coordinate(44.809000, 20.382729),
                                        new Coordinate(44.832378, 20.433712)),
                        List.of(
                                new BoundingBox(new Coordinate(44.809, 20.382729),
                                                new Coordinate(44.832378, 20.433712))
                        ))
        );
    }

    @Autowired
    OsmClient osmClient;

    @ParameterizedTest
    @MethodSource("boundingBoxesWithResult")
    void split_DifferentBoxes_ValidSplitting(BoundingBox box, List<BoundingBox> expected) {
        var target = new BoundingBoxOsmElementsSplitter(100000, new MetersDistance(50), osmClient);

        var actual = target.split(box);

        assertThat(actual).hasSameSizeAs(expected).containsAll(expected);
    }

}