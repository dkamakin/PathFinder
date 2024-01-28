package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import com.github.pathfinder.indexer.database.repository.RegionSearcherRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class RegionTestTemplate {

    private final RegionSearcherRepository repository;

    @Transactional
    public RegionEntity save(RegionEntity entity) {
        return repository.save(entity);
    }

    @ReadTransactional
    public Optional<RegionEntity> find(int id) {
        return repository.findById(id);
    }

}
