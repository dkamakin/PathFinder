package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.core.tools.impl.BoundingBox;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.RegionTestTemplate;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.service.BoundingBoxSplitter;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IndexerServiceDatabaseTest
@Import({RegionSplitProcessor.class, RegionTestTemplate.class})
class RegionSplitProcessorTest {

    @Autowired
    RegionSplitProcessor target;

    @MockBean
    BoundingBoxSplitterFactory splitterFactory;

    @Autowired
    RegionTestTemplate testTemplate;

    @MockBean
    IndexBoxUpdaterService boxUpdaterService;

    BoundingBoxSplitter splitter() {
        return mock(BoundingBoxSplitter.class);
    }

    void whenNeedToCreateSplitter(BoundingBoxSplitter expected) {
        when(splitterFactory.create()).thenReturn(expected);
    }

    void whenNeedToSplit(BoundingBoxSplitter splitter, BoundingBox box, List<BoundingBox> expected) {
        when(splitter.split(box)).thenReturn(expected);
    }

    @Test
    void split_RegionWasSplitToSomeBoxes_SaveBoxesAndMarkAsProcessed() {
        var splitter    = splitter();
        var entity      = testTemplate.save(RegionEntity.builder().min(1, 2).max(3, 4).build());
        var boundingBox = new BoundingBox(new Coordinate(1, 2), new Coordinate(3, 4));

        assertThat(testTemplate.find(entity.getId())).get().matches(Predicate.not(RegionEntity::isProcessed));

        whenNeedToCreateSplitter(splitter);
        whenNeedToSplit(splitter, boundingBox, List.of(new BoundingBox(new Coordinate(1, 2),
                                                                       new Coordinate(3, 4))));

        target.split(entity);

        verify(splitter).split(boundingBox);
        assertThat(testTemplate.find(entity.getId())).get().matches(RegionEntity::isProcessed);

        verify(boxUpdaterService).save(IndexBoxEntity.builder().min(1, 2).max(3, 4).build());
    }

}