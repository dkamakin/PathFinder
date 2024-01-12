package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.repository.IndexBoxRepository;
import com.github.pathfinder.indexer.service.BoxService;
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
public class IndexBoxService implements BoxService {

    private final IndexBoxRepository boxRepository;
    private final IDateTimeSupplier  dateTimeSupplier;

    @Logged
    @Override
    @ReadTransactional
    public List<IndexBoxEntity> all() {
        return Lists.newArrayList(boxRepository.findAll());
    }

    @Override
    @ReadTransactional
    @Logged(ignoreReturnValue = false, value = {"saveDelay", "connectDelay"})
    public List<IndexBoxEntity> operableBoxes(Duration saveDelay, Duration connectDelay) {
        return boxRepository.operableBoxes(saveDelay, connectDelay, dateTimeSupplier.now());
    }

    @Override
    @ReadTransactional
    @Logged(ignoreReturnValue = false, value = "id")
    public Optional<IndexBoxEntity> box(Integer id) {
        return boxRepository.findById(id);
    }

}
