package com.github.pathfinder.mapper;

import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.ChunkPointRelation;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.GetChunksResponse;
import com.github.pathfinder.searcher.api.data.point.Point;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NodeMapper {

    NodeMapper MAPPER = Mappers.getMapper(NodeMapper.class);

    default List<PointNode> pointNodes(List<Point> requests) {
        return requests.stream().map(this::pointNode).toList();
    }

    default PointNode pointNode(Point point) {
        return PointNode.builder()
                .location(point.coordinate().latitude(), point.coordinate().longitude(), point.altitude())
                .passabilityCoefficient(point.passabilityCoefficient())
                .landType(point.landType())
                .build();
    }

    default GetChunksResponse getChunksResponse(List<ChunkNode> chunks) {
        return new GetChunksResponse(chunks(chunks));
    }

    Set<ChunkPointRelation> chunkRelations(List<PointNode> nodes);

    default ChunkPointRelation chunkRelation(PointNode node) {
        return new ChunkPointRelation(node);
    }

    List<Chunk> chunks(List<ChunkNode> chunks);

    @Mapping(target = "isConnected", source = "connected")
    Chunk chunk(ChunkNode node);

}
