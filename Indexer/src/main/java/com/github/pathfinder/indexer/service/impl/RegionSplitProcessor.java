package com.github.pathfinder.indexer.service.impl;

import static com.github.pathfinder.indexer.data.EntityMapper.MAPPER;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.data.BoundingBox;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.database.repository.RegionUpdaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RefreshScope
@RequiredArgsConstructor
public class RegionSplitProcessor {

    private final RegionUpdaterRepository    regionUpdaterRepository;
    private final IndexBoxUpdaterService     boxUpdaterService;
    private final BoundingBoxSplitterFactory splitterFactory;

    @Logged
    @Transactional
    public void split(RegionEntity region) {
        var splitter = splitterFactory.create();

        splitter.split(MAPPER.boundingBox(region)).stream()
                .map(box -> indexBoxEntity(box, region))
                .forEach(boxUpdaterService::save);

        regionUpdaterRepository.save(region.processed());
    }

    private IndexBoxEntity indexBoxEntity(BoundingBox box, RegionEntity region) {
        return IndexBoxEntity.builder()
                .min(box.min().latitude(), box.min().longitude())
                .max(box.max().latitude(), box.max().longitude())
                .region(region)
                .build();
    }

}
