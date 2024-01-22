package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.repository.IndexBoxSearcherRepository;
import com.github.pathfinder.indexer.service.BoxSearcherService;
import com.google.common.collect.Lists;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class IndexBoxSearcherService implements BoxSearcherService {

    private final IndexBoxSearcherRepository boxRepository;
    private final IDateTimeSupplier          dateTimeSupplier;

    @Logged
    @Override
    @ReadTransactional
    public List<IndexBoxEntity> all() {
        return Lists.newArrayList(boxRepository.findAll());
    }

    @Override
    @ReadTransactional
    @Logged(ignoreReturnValue = false, value = {"saveDelay"})
    public List<IndexBoxEntity> savable(Duration saveDelay) {
        return boxRepository.savable(saveDelay, dateTimeSupplier.now());
    }

    @Override
    @ReadTransactional
    @Logged(ignoreReturnValue = false, value = {"connectDelay"})
    public List<IndexBoxEntity> connectable(Duration connectDelay) {
        return boxRepository.connectable(connectDelay, dateTimeSupplier.now());
    }

    @Override
    @ReadTransactional
    @Logged(ignoreReturnValue = false, value = "id")
    public Optional<IndexBoxEntity> box(Integer id) {
        return boxRepository.findById(id);
    }

}
