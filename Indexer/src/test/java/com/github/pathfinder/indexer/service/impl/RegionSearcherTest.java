package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.RegionTestTemplate;
import com.github.pathfinder.indexer.database.entity.RegionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@IndexerServiceDatabaseTest
@Import({RegionSearcher.class, RegionTestTemplate.class})
class RegionSearcherTest {

    @Autowired
    RegionSearcher target;

    @Autowired
    RegionTestTemplate testTemplate;

    RegionEntity regionEntity(boolean processed) {
        return RegionEntity.builder()
                .processed(processed)
                .max(1, 1)
                .min(1, 1)
                .build();
    }

    @Test
    void nextNotProcessed_NoRegions_EmptyResult() {
        assertThat(target.nextNotProcessed()).isEmpty();
    }

    @Test
    void nextNotProcessed_SingleEntityFound_ReturnEntity() {
        var expected = regionEntity(false);

        testTemplate.save(expected);

        var actual = target.nextNotProcessed();

        assertThat(actual).contains(expected);
    }

    @Test
    void nextNotProcessed_MultipleEntitiesFound_ReturnFirstEntity() {
        testTemplate.save(regionEntity(true));
        testTemplate.save(regionEntity(true));
        var expected = testTemplate.save(regionEntity(false));
        testTemplate.save(regionEntity(false));

        var actual = target.nextNotProcessed();

        assertThat(actual).contains(expected);
    }

    @Test
    void nextNotProcessed_AllEntitiesProcessed_EmptyResult() {
        testTemplate.save(regionEntity(true));
        testTemplate.save(regionEntity(true));
        testTemplate.save(regionEntity(true));
        testTemplate.save(regionEntity(true));

        var actual = target.nextNotProcessed();

        assertThat(actual).isEmpty();
    }

}