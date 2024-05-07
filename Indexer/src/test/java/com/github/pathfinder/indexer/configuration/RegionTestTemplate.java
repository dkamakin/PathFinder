package com.github.pathfinder.indexer.configuration;

import java.util.Optional;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class RegionTestTemplate {

    private final RegionTestSearcherRepository repository;

    @Transactional
    public RegionEntity randomRegion() {
        return save(RegionEntity.builder()
                            .max(Math.random(), Math.random())
                            .min(Math.random(), Math.random())
                            .build());
    }

    @Transactional
    public RegionEntity save(RegionEntity entity) {
        return repository.save(entity);
    }

    @ReadTransactional
    public Optional<RegionEntity> find(int id) {
        return repository.findById(id);
    }

    @ReadTransactional
    public Optional<RegionEntity> findEager(int id) {
        return repository.findEager(id);
    }

}
