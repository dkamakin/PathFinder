package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.core.tools.IDateTimeSupplier;
import com.github.pathfinder.indexer.configuration.IndexerServiceDatabaseTest;
import com.github.pathfinder.indexer.configuration.IndexerStateBuilder;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.service.BoxSearcherService;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@IndexerServiceDatabaseTest
@Import({IndexBoxSearcherService.class})
class IndexBoxSearcherServiceTest {

    @Autowired
    IndexerStateBuilder stateBuilder;

    @Autowired
    BoxSearcherService target;

    @MockBean
    IDateTimeSupplier dateTimeSupplier;

    void whenNeedToGetNow(Instant now) {
        when(dateTimeSupplier.now()).thenReturn(now);
    }

    @Test
    void all_NothingFound_EmptyResult() {
        assertThat(target.all()).isEmpty();
    }

    @Test
    void all_SomeBoxesAreFound_ReturnBoxes() {
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saveRequestTimestamp(Instant.now())
                                                 .connectionRequestTimestamp(Instant.now())
                                                 .saved(true)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        var actual = target.all();

        assertThat(actual).hasSize(1).first().isEqualTo(expected);
    }

    @Test
    void box_BoxIsNotFound_EmptyResult() {
        assertThat(target.box(2399)).isEmpty();
    }

    @Test
    void box_BoxIsFound_ReturnResult() {
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saveRequestTimestamp(Instant.now())
                                                 .connectionRequestTimestamp(Instant.now())
                                                 .saved(true)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        var actual = target.box(expected.getId());

        assertThat(actual).contains(expected);
    }

    @Test
    void savable_BoxWasNeverSentToSave_ReturnBox() {
        var now = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saved(false)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        whenNeedToGetNow(now);

        var actual = target.savable(Duration.ofMinutes(30));

        assertThat(actual).hasSize(1).first().isEqualTo(expected);
    }

    @Test
    void savable_BoxesAreJustSaved_NothingReturned() {
        var now = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        stateBuilder.save(IndexBoxEntity.builder()
                                  .saveRequestTimestamp(now)
                                  .connectionRequestTimestamp(now)
                                  .saved(false)
                                  .connected(false)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());

        whenNeedToGetNow(now);

        var actual = target.savable(Duration.ofMinutes(30));

        assertThat(actual).isEmpty();
    }

    @Test
    void savable_BoxWasSentForSavingLongTimeAgo_BoxIsReturned() {
        var saveDelay = Duration.ofSeconds(30);
        var now       = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saveRequestTimestamp(now)
                                                 .saved(false)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        whenNeedToGetNow(now.plus(saveDelay).plus(Duration.ofMinutes(30)));

        var actual = target.savable(saveDelay);

        assertThat(actual).hasSize(1).first().isEqualTo(expected);
    }

    @Test
    void connectable_BoxWasSentForConnectionLongTimeAgo_BoxIsReturned() {
        var connectionDelay = Duration.ofSeconds(30);
        var now             = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .connectionRequestTimestamp(now)
                                                 .saved(true)
                                                 .connected(false)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        whenNeedToGetNow(now.plus(connectionDelay).plus(Duration.ofMinutes(30)));

        var actual = target.connectable(connectionDelay);

        assertThat(actual).hasSize(1).first().isEqualTo(expected);
    }

    @Test
    void connectable_BoxWasNeverSentForConnection_ReturnBox() {
        var connectionDelay = Duration.ofSeconds(30);
        var now             = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        var expected = stateBuilder.save(IndexBoxEntity.builder()
                                                 .saved(true)
                                                 .max(12, 23)
                                                 .min(32, 43)
                                                 .build());

        whenNeedToGetNow(now.plus(connectionDelay).plus(Duration.ofMinutes(30)));

        var actual = target.connectable(connectionDelay);

        assertThat(actual).hasSize(1).first().isEqualTo(expected);
    }

    @Test
    void connectable_BoxWasConnectedLongTimeAgo_NothingReturned() {
        var connectionDelay = Duration.ofSeconds(30);
        var now             = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        stateBuilder.save(IndexBoxEntity.builder()
                                  .connectionRequestTimestamp(now)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());

        whenNeedToGetNow(now.plus(connectionDelay).plus(Duration.ofMinutes(30)));

        var actual = target.connectable(connectionDelay);

        assertThat(actual).isEmpty();
    }

    @Test
    void connectable_BoxIsNotSaved_NothingReturned() {
        var connectionDelay = Duration.ofSeconds(30);
        var now             = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(false)
                                  .connected(false)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());

        whenNeedToGetNow(now.plus(connectionDelay).plus(Duration.ofMinutes(30)));

        var actual = target.connectable(connectionDelay);

        assertThat(actual).isEmpty();
    }

    @Test
    void connectable_BoxesAreJustConnected_NothingReturned() {
        var now = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(true)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        stateBuilder.save(IndexBoxEntity.builder()
                                  .saveRequestTimestamp(now)
                                  .connectionRequestTimestamp(now)
                                  .connected(false)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());

        whenNeedToGetNow(now);

        var actual = target.connectable(Duration.ofMinutes(30));

        assertThat(actual).isEmpty();
    }

    @Test
    void connectable_OneOfTheBoxesIsNotSaved_EmptyResult() {
        var now = Instant.now();

        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(false)
                                  .connected(false)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());
        stateBuilder.save(IndexBoxEntity.builder()
                                  .saved(true)
                                  .connected(false)
                                  .max(12, 23)
                                  .min(32, 43)
                                  .build());

        whenNeedToGetNow(now);

        var actual = target.connectable(Duration.ofMinutes(30));

        assertThat(actual).isEmpty();
    }

}
