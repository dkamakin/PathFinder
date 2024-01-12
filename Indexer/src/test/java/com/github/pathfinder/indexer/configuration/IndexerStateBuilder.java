package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntityBuilder;
import com.github.pathfinder.indexer.database.repository.IndexBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class IndexerStateBuilder {

    private final IndexBoxRepository boxRepository;

    @Transactional
    public IndexBoxEntity save(IndexBoxEntity box) {
        return boxRepository.save(box);
    }

    @Transactional
    public IndexBoxEntity randomCoords(IndexBoxEntityBuilder builder) {
        return boxRepository.save(builder.max(Math.random(), Math.random())
                                          .min(Math.random(), Math.random())
                                          .build());
    }

}
