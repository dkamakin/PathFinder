package com.github.pathfinder.indexer.database.entity;

import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = IndexBoxEntity.Token.TABLE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexBoxEntity {

    @UtilityClass
    public static class Token {

        public static final String TABLE                        = "INDEX_BOX";
        public static final String ID                           = "ID";
        public static final String ID_GENERATOR                 = "INDEX_BOX_ID_GENERATOR";
        public static final String ID_SEQUENCE                  = "INDEX_BOX_ID_SEQUENCE";
        public static final String SAVED                        = "SAVED";
        public static final String CONNECTED                    = "CONNECTED";
        public static final String SAVE_REQUEST_TIMESTAMP       = "SAVE_REQUEST_TIMESTAMP";
        public static final String CONNECTION_REQUEST_TIMESTAMP = "CONNECTION_REQUEST_TIMESTAMP";

    }

    @Id
    @Column(name = Token.ID)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Token.ID_GENERATOR)
    @SequenceGenerator(name = Token.ID_GENERATOR, sequenceName = Token.ID_SEQUENCE, allocationSize = 1)
    private Integer id;

    @Column(name = Token.SAVED)
    private boolean saved;

    @Column(name = Token.CONNECTED)
    private boolean connected;

    @Column(name = Token.SAVE_REQUEST_TIMESTAMP)
    private Instant saveRequestTimestamp;

    @Column(name = Token.CONNECTION_REQUEST_TIMESTAMP)
    private Instant connectionRequestTimestamp;

    @Valid
    @NotNull
    @Embedded
    private MinBoxCoordinate min;

    @Valid
    @NotNull
    @Embedded
    private MaxBoxCoordinate max;

    public static IndexBoxEntityBuilder builder() {
        return new IndexBoxEntityBuilder();
    }

    public IndexBoxEntity setSaved(boolean saved) {
        return setAndReturnThis(saved, x -> this.saved = x);
    }

    public IndexBoxEntity setConnected(boolean connected) {
        return setAndReturnThis(connected, x -> this.connected = x);
    }

    public IndexBoxEntity setSaveRequestTimestamp(Instant saveRequestTimestamp) {
        return setAndReturnThis(saveRequestTimestamp, x -> this.saveRequestTimestamp = x);
    }

    public IndexBoxEntity setConnectionRequestTimestamp(Instant connectionRequestTimestamp) {
        return setAndReturnThis(connectionRequestTimestamp, x -> this.connectionRequestTimestamp = x);
    }

    private <T> IndexBoxEntity setAndReturnThis(T value, Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndexBoxEntity that = (IndexBoxEntity) o;
        return saved == that.saved &&
                connected == that.connected &&
                Objects.equal(id, that.id) &&
                Objects.equal(min, that.min) &&
                Objects.equal(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, saved, connected, min, max);
    }

}
