package com.github.pathfinder.indexer.service.impl;

import java.util.Optional;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.database.repository.RegionSearcherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionSearcher {

    private final RegionSearcherRepository searcherRepository;

    @ReadTransactional
    @Logged(ignoreReturnValue = false)
    public Optional<RegionEntity> nextNotProcessed() {
        return searcherRepository.nextNotProcessed();
    }

}
