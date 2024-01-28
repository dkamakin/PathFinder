package com.github.pathfinder.indexer.osm.impl;

import com.github.pathfinder.indexer.client.osm.impl.OverpassQueryBuilder;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OverpassQueryBuilderTest {

    @Test
    void way_BoxProvided_BuildValidQuery() {
        var min = new OsmBox.OsmBoxCoordinate(1, 2);
        var max = new OsmBox.OsmBoxCoordinate(3, 4);
        var box = new OsmBox(min, max);

        assertThat(new OverpassQueryBuilder().way(box).asBody())
                .isEqualTo("(way(1.0,2.0,3.0,4.0););out body;");
    }

    @Test
    void node_BoxProvided_BuildValidQuery() {
        var min = new OsmBox.OsmBoxCoordinate(1, 2);
        var max = new OsmBox.OsmBoxCoordinate(3, 4);
        var box = new OsmBox(min, max);

        assertThat(new OverpassQueryBuilder().node(box).asBody())
                .isEqualTo("(node(1.0,2.0,3.0,4.0););out body;");
    }

    @Test
    void node_CombinationWithWay_BuildValidQuery() {
        var nodeBox = new OsmBox(new OsmBox.OsmBoxCoordinate(1, 2), new OsmBox.OsmBoxCoordinate(3, 4));
        var wayBox  = new OsmBox(new OsmBox.OsmBoxCoordinate(5, 6), new OsmBox.OsmBoxCoordinate(7, 8));

        assertThat(new OverpassQueryBuilder().node(nodeBox).way(wayBox).asBody())
                .isEqualTo("(node(1.0,2.0,3.0,4.0);way(5.0,6.0,7.0,8.0););out body;");
    }

    @Test
    void node_OutCount_BuildValidQuery() {
        var nodeBox = new OsmBox(new OsmBox.OsmBoxCoordinate(1, 2), new OsmBox.OsmBoxCoordinate(3, 4));
        var wayBox  = new OsmBox(new OsmBox.OsmBoxCoordinate(5, 6), new OsmBox.OsmBoxCoordinate(7, 8));

        assertThat(new OverpassQueryBuilder().node(nodeBox).way(wayBox).asCount())
                .isEqualTo("(node(1.0,2.0,3.0,4.0);way(5.0,6.0,7.0,8.0););out count;");
    }

    @Test
    void nodes_ListOfIdsProvided_buildValidQuery() {
        var ids = List.of(1L, 2L);

        assertThat(new OverpassQueryBuilder().nodes(ids).asBody())
                .isEqualTo("(node(1);node(2););out body;");
    }

}
