package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilderConfiguration;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@IndexerServiceDatabaseTest
@IndexerStateBuilderConfiguration
@Import({IndexBoxSearcherService.class})
class IndexBoxUpdaterTest {

    @Autowired
    IndexBoxUpdaterService target;

    @Autowired
    IndexBoxSearcherService searcherService;

    @Test
    void save_StateHasChanged_SaveChangedInstance() {
        var entity = target.save(IndexBoxEntity.builder()
                                         .min(1, 2)
                                         .max(3, 4)
                                         .connected(false).build());

        assertThat(searcherService.box(entity.getId()))
                .get()
                .matches(Predicate.not(IndexBoxEntity::isConnected));

        var saved = target.save(entity.setConnected(true));

        assertThat(saved).matches(IndexBoxEntity::isConnected);

        assertThat(searcherService.box(entity.getId()))
                .get()
                .matches(IndexBoxEntity::isConnected);
    }

}
