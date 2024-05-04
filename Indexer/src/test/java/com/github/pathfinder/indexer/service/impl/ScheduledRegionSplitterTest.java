package com.github.pathfinder.indexer.service.impl;

import java.util.Optional;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.scheduled.ScheduledRegionSplitter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduledRegionSplitterTest {

    @InjectMocks
    ScheduledRegionSplitter target;

    @Mock
    RegionSearcher regionSearcher;

    @Mock
    RegionSplitProcessor regionSplitProcessor;

    void whenNeedToGetRegion(Optional<RegionEntity> entity, Optional<RegionEntity>... other) {
        when(regionSearcher.nextNotProcessed()).thenReturn(entity, other);
    }

    void whenNeedToThrowOnSplit(RegionEntity entity, RuntimeException expected) {
        doThrow(expected).when(regionSplitProcessor).split(entity);
    }

    RegionEntity regionEntity() {
        return mock(RegionEntity.class);
    }

    @Test
    void run_FailedToSplit_SkipAndGoNext() {
        var invalidRegion = regionEntity();
        var validRegion   = regionEntity();

        whenNeedToGetRegion(Optional.of(invalidRegion), Optional.of(validRegion), Optional.empty());
        whenNeedToThrowOnSplit(invalidRegion, new IllegalArgumentException());

        target.run();

        verify(regionSplitProcessor).split(validRegion);
    }

    @Test
    void run_NothingFound_NoActions() {
        whenNeedToGetRegion(Optional.empty());

        target.run();

        verifyNoInteractions(regionSplitProcessor);
    }

    @Test
    void run_RegionIsFound_ProcessARegion() {
        var expected = regionEntity();

        whenNeedToGetRegion(Optional.of(expected), Optional.empty());

        target.run();

        verify(regionSplitProcessor).split(expected);
    }

    @Test
    void run_MultipleRegionsFound_ProcessAllRegions() {
        var first  = regionEntity();
        var second = regionEntity();

        whenNeedToGetRegion(Optional.of(first), Optional.of(second), Optional.empty());

        target.run();

        verify(regionSplitProcessor).split(first);
        verify(regionSplitProcessor).split(second);
    }

}