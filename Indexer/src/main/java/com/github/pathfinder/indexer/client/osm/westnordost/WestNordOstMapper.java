package com.github.pathfinder.indexer.client.osm.westnordost;

import com.github.pathfinder.indexer.data.osm.OsmNode;
import com.github.pathfinder.indexer.data.osm.OsmTags;
import com.github.pathfinder.indexer.data.osm.OsmWay;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Way;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WestNordOstMapper {

    WestNordOstMapper MAPPER = Mappers.getMapper(WestNordOstMapper.class);

    @Mapping(target = "coordinate", source = "position")
    @Mapping(target = "id", source = "id")
    OsmNode osmNode(Node node);

    default OsmTags tags(Map<String, String> tags) {
        return new OsmTags(tags);
    }

    OsmWay osmWay(Way way);

}
