package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.tools.impl.BoundingBox;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.database.repository.RegionUpdaterRepository;
import com.github.pathfinder.indexer.service.BoxUpdaterService;
import com.github.pathfinder.indexer.service.IRegionSplitProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.github.pathfinder.indexer.data.EntityMapper.MAPPER;

@Service
@RefreshScope
@RequiredArgsConstructor
public class RegionSplitProcessor implements IRegionSplitProcessor {

    private final RegionUpdaterRepository    regionUpdaterRepository;
    private final BoxUpdaterService          boxUpdaterService;
    private final BoundingBoxSplitterFactory splitterFactory;

    @Logged
    @Override
    @Transactional
    public void split(RegionEntity entity) {
        var splitter = splitterFactory.create();

        splitter.split(MAPPER.boundingBox(entity)).stream().map(this::indexBoxEntity).forEach(boxUpdaterService::save);

        regionUpdaterRepository.save(entity.processed());
    }

    private IndexBoxEntity indexBoxEntity(BoundingBox box) {
        return IndexBoxEntity.builder()
                .min(box.min().latitude(), box.min().longitude())
                .max(box.max().latitude(), box.max().longitude())
                .build();
    }

}
