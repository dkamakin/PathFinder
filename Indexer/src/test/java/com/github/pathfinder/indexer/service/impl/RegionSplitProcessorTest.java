package com.github.pathfinder.indexer.service.impl;

import java.util.List;
import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.core.data.Coordinate;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.RegionTestTemplate;
import com.github.pathfinder.indexer.database.entity.MaxBoxCoordinate;
import com.github.pathfinder.indexer.database.entity.MinBoxCoordinate;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.service.BoundingBoxSplitter;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;

@IndexerServiceDatabaseTest
@Import({RegionSplitProcessor.class, RegionTestTemplate.class, IndexBoxUpdaterService.class,
        IndexBoxSearcherService.class})
class RegionSplitProcessorTest {

    @Autowired
    RegionSplitProcessor target;

    @MockBean
    BoundingBoxSplitterFactory splitterFactory;

    @Autowired
    RegionTestTemplate regionTestTemplate;

    @SpyBean
    IndexBoxUpdaterService boxUpdaterService;

    @Autowired
    IndexBoxSearcherService boxSearcherService;

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
        var boundingBox = new BoundingBox(new Coordinate(1, 2), new Coordinate(3, 4));
        var entity = regionTestTemplate.save(RegionEntity.builder()
                                                     .min(boundingBox.min().latitude(),
                                                          boundingBox.min().longitude())
                                                     .max(boundingBox.max().latitude(),
                                                          boundingBox.max().longitude())
                                                     .build());

        assertThat(regionTestTemplate.find(entity.getId())).get().matches(Predicate.not(RegionEntity::isProcessed));

        whenNeedToCreateSplitter(splitter);
        whenNeedToSplit(splitter, boundingBox, List.of(boundingBox));

        target.split(entity);

        var region      = regionTestTemplate.find(entity.getId()).get();
        var regionBoxes = region.getBoxes();

        assertThatThrownBy(regionBoxes::size).isInstanceOf(LazyInitializationException.class);
        assertThat(region).matches(RegionEntity::isProcessed);

        assertThat(regionTestTemplate.findEager(entity.getId()))
                .get()
                .satisfies(x -> assertThat(x.getBoxes()).hasSize(1));

        var box = boxSearcherService.all();

        assertThat(box)
                .hasSize(1)
                .first()
                .matches(x -> x.getMin().equals(new MinBoxCoordinate(boundingBox.min().latitude(),
                                                                     boundingBox.min().longitude())))
                .matches(x -> x.getMax().equals(new MaxBoxCoordinate(boundingBox.max().latitude(),
                                                                     boundingBox.max().longitude())))
                .matches(x -> x.getRegion().equals(region));
    }

}