package com.github.pathfinder.indexer.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import com.github.pathfinder.core.aspect.Logged;
import com.github.pathfinder.core.interfaces.ReadTransactional;
import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.repository.IndexBoxSearcherRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class IndexBoxSearcherService {

    private final IndexBoxSearcherRepository boxRepository;
    private final IDateTimeSupplier          dateTimeSupplier;

    @Logged
    @ReadTransactional
    public List<IndexBoxEntity> all() {
        return Lists.newArrayList(boxRepository.findAll());
    }

    @ReadTransactional
    @Logged(value = {"saveDelay"})
    public List<IndexBoxEntity> savable(Duration saveDelay) {
        return boxRepository.savable(saveDelay, dateTimeSupplier.now());
    }

    @ReadTransactional
    @Logged(value = {"id", "saveDelay"}, ignoreReturnValue = false)
    public boolean isSavable(IndexBoxEntity box, Duration saveDelay) {
        return isSavable(box.getId(), saveDelay);
    }

    @ReadTransactional
    @Logged(value = {"id", "saveDelay"}, ignoreReturnValue = false)
    public boolean isSavable(int id, Duration saveDelay) {
        return boxRepository.isSavable(saveDelay, dateTimeSupplier.now(), id);
    }

    @ReadTransactional
    @Logged(value = {"connectDelay"})
    public List<IndexBoxEntity> connectable(Duration connectDelay) {
        var notSaved = boxRepository.countNotSaved();

        if (notSaved > 0) {
            log.info("{} boxes are not saved yet, connection is not possible", notSaved);
            return List.of();
        }

        return boxRepository.connectable(connectDelay, dateTimeSupplier.now());
    }

    @ReadTransactional
    @Logged(value = "id")
    public Optional<IndexBoxEntity> box(Integer id) {
        return boxRepository.findById(id);
    }

}
