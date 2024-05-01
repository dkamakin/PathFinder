package com.github.pathfinder.indexer.scheduled;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.indexer.configuration.SplitterConfiguration;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.service.impl.RegionSearcher;
import com.github.pathfinder.indexer.service.impl.RegionSplitProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledRegionSplitter {

    private final RegionSearcher       regionSearcher;
    private final RegionSplitProcessor regionSplitProcessor;

    @Logged
    @Scheduled(fixedDelayString = SplitterConfiguration.SPLITTER_DELAY)
    public void run() {
        var region = regionSearcher.nextNotProcessed();

        while (region.isPresent()) {
            split(region.get());
            region = regionSearcher.nextNotProcessed();
        }
    }

    private void split(RegionEntity entity) {
        try {
            regionSplitProcessor.split(entity);
        } catch (Exception e) {
            log.error("Failed to split a region", e);
        }
    }

}
