package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.database.repository.RegionSearcherRepository;
import com.github.pathfinder.indexer.service.IRegionSearcher;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionSearcher implements IRegionSearcher {

    private final RegionSearcherRepository searcherRepository;

    @Override
    @ReadTransactional
    @Logged(ignoreReturnValue = false)
    public Optional<RegionEntity> nextNotProcessed() {
        return searcherRepository.nextNotProcessed();
    }

}
