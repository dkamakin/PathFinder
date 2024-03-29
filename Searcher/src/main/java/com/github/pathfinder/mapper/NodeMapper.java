package com.github.pathfinder.mapper;

import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.database.node.projection.SimpleChunk;
import com.github.pathfinder.searcher.api.data.Chunk;
import com.github.pathfinder.searcher.api.data.GetChunksResponse;
import com.github.pathfinder.searcher.api.data.IndexBox;
import com.github.pathfinder.searcher.api.data.point.Point;
import java.util.List;
import org.mapstruct.Mapper;
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

    default GetChunksResponse getChunksResponse(List<SimpleChunk> chunks) {
        return new GetChunksResponse(chunks(chunks));
    }

    default ChunkNode chunkNode(IndexBox box, List<Point> nodes) {
        return ChunkNode.builder()
                .id(box.id())
                .points(pointNodes(nodes))
                .max(box.max())
                .min(box.min())
                .build();
    }

    List<Chunk> chunks(List<SimpleChunk> chunks);

    Chunk chunk(SimpleChunk node);

}
