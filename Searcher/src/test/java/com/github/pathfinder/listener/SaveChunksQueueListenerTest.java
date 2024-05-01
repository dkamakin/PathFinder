package com.github.pathfinder.listener;

import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import com.github.pathfinder.PointFixtures;
import com.github.pathfinder.configuration.SearcherAmqpTest;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.database.node.ChunkNode;
import com.github.pathfinder.database.node.PointNode;
import com.github.pathfinder.messaging.test.MessagingTestConstant;
import com.github.pathfinder.searcher.api.SearcherApi;
import com.github.pathfinder.searcher.api.data.IndexBox;
import com.github.pathfinder.searcher.api.data.point.Point;
import com.github.pathfinder.searcher.api.data.point.SavePointsMessage;
import com.github.pathfinder.service.impl.ChunkUpdaterService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;

@SearcherAmqpTest
class SaveChunksQueueListenerTest {

    @Autowired
    SearcherApi searcherApi;

    @Autowired
    ChunkUpdaterService chunkUpdaterService;

    @Captor
    ArgumentCaptor<ChunkNode> chunkNodeCaptor;

    @Test
    void save_ValidRequest_CallService() {
        var point = new Point(1D, new Coordinate(2D, 3D), "BAY", 4D);
        var pointNode = PointNode.builder()
                .landType(point.landType())
                .location(point.coordinate().latitude(), point.coordinate().longitude(), point.altitude())
                .passabilityCoefficient(point.passabilityCoefficient())
                .build();
        var box = new IndexBox(1, PointFixtures.randomCoordinate(), PointFixtures.randomCoordinate());
        var chunk = ChunkNode.builder()
                .id(box.id())
                .min(box.min())
                .max(box.max())
                .points(List.of(pointNode))
                .build();

        searcherApi.save(new SavePointsMessage(box, List.of(point)));

        verify(chunkUpdaterService, timeout(MessagingTestConstant.DEFAULT_TIMEOUT.toMillis()))
                .save(chunkNodeCaptor.capture());

        assertThat(chunkNodeCaptor.getValue())
                .isEqualTo(chunk)
                .satisfies(x -> assertThat(x.getPoints())
                        .hasSize(1)
                        .first()
                        .usingRecursiveComparison()
                        .ignoringFieldsOfTypes(UUID.class)
                        .isEqualTo(pointNode));
    }

}
