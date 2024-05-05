package com.github.pathfinder.indexer.osm.impl;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.pathfinder.indexer.client.osm.impl.OverpassQueryBuilder;
import com.github.pathfinder.indexer.data.osm.OsmBox;
import com.github.pathfinder.indexer.data.osm.OsmQueryTag;
import org.junit.jupiter.api.Test;

class OverpassQueryBuilderTest {

    static final OsmBox.OsmBoxCoordinate MIN        = new OsmBox.OsmBoxCoordinate(39.482845, -0.677032);
    static final OsmBox.OsmBoxCoordinate MAX        = new OsmBox.OsmBoxCoordinate(39.678655, -0.348816);
    static final List<OsmQueryTag>       TAG_VALUES = List.of(
            new OsmQueryTag("natural", List.of("beach", "sand")),
            new OsmQueryTag("highway", List.of("unclassified"))
    );

    @Test
    void way_BoxProvided_BuildValidQuery() {
        var box = new OsmBox(MIN, MAX);

        assertThat(new OverpassQueryBuilder().way(box, TAG_VALUES).asBody())
                .isEqualTo(
                        "(way(39.482845,-0.677032,39.678655,-0.348816)[natural~\"beach|sand\"];way(39.482845,-0.677032,39.678655,-0.348816)[highway~\"unclassified\"];);out body;");
    }

    @Test
    void node_BoxProvided_BuildValidQuery() {
        var box = new OsmBox(MIN, MAX);

        assertThat(new OverpassQueryBuilder().node(box, TAG_VALUES).asBody())
                .isEqualTo(
                        "(node(39.482845,-0.677032,39.678655,-0.348816)[natural~\"beach|sand\"];node(39.482845,-0.677032,39.678655,-0.348816)[highway~\"unclassified\"];);out body;");
    }

    @Test
    void node_CombinationWithWay_BuildValidQuery() {
        var nodeBox = new OsmBox(new OsmBox.OsmBoxCoordinate(1, 2), new OsmBox.OsmBoxCoordinate(3, 4));
        var wayBox  = new OsmBox(new OsmBox.OsmBoxCoordinate(5, 6), new OsmBox.OsmBoxCoordinate(7, 8));

        assertThat(new OverpassQueryBuilder().node(nodeBox, TAG_VALUES).way(wayBox, TAG_VALUES).asBody())
                .isEqualTo(
                        "(node(1.0,2.0,3.0,4.0)[natural~\"beach|sand\"];node(1.0,2.0,3.0,4.0)[highway~\"unclassified\"];way(5.0,6.0,7.0,8.0)[natural~\"beach|sand\"];way(5.0,6.0,7.0,8.0)[highway~\"unclassified\"];);out body;");
    }

    @Test
    void node_OutCount_BuildValidQuery() {
        var nodeBox = new OsmBox(new OsmBox.OsmBoxCoordinate(1, 2), new OsmBox.OsmBoxCoordinate(3, 4));
        var wayBox  = new OsmBox(new OsmBox.OsmBoxCoordinate(5, 6), new OsmBox.OsmBoxCoordinate(7, 8));

        assertThat(new OverpassQueryBuilder().node(nodeBox, TAG_VALUES).way(wayBox, TAG_VALUES).asCount())
                .isEqualTo(
                        "(node(1.0,2.0,3.0,4.0)[natural~\"beach|sand\"];node(1.0,2.0,3.0,4.0)[highway~\"unclassified\"];way(5.0,6.0,7.0,8.0)[natural~\"beach|sand\"];way(5.0,6.0,7.0,8.0)[highway~\"unclassified\"];);out count;");
    }

    @Test
    void nodes_ListOfIdsProvided_buildValidQuery() {
        var ids = List.of(1L, 2L);

        assertThat(new OverpassQueryBuilder().nodes(ids).asBody())
                .isEqualTo("(node(1);node(2););out body;");
    }

}
