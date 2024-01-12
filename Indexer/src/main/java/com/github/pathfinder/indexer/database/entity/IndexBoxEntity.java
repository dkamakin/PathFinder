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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.UtilityClass;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = IndexBoxEntity.Token.TABLE)
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

    @Setter
    @Column(name = Token.SAVED)
    private boolean saved;

    @Setter
    @Column(name = Token.CONNECTED)
    private boolean connected;

    @Setter
    @Column(name = Token.SAVE_REQUEST_TIMESTAMP)
    private Instant saveRequestTimestamp;

    @Setter
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

    public IndexBoxEntity(boolean saved, boolean connected, Instant saveRequestTimestamp,
                          Instant connectionRequestTimestamp, MinBoxCoordinate min, MaxBoxCoordinate max) {
        this.saved                      = saved;
        this.connected                  = connected;
        this.saveRequestTimestamp       = saveRequestTimestamp;
        this.connectionRequestTimestamp = connectionRequestTimestamp;
        this.min                        = min;
        this.max                        = max;
    }

    public static IndexBoxEntityBuilder builder() {
        return new IndexBoxEntityBuilder();
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
