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
@Table(name = RegionEntity.Token.TABLE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionEntity {

    @UtilityClass
    public static class Token {

        public static final String TABLE        = "REGION";
        public static final String ID           = "ID";
        public static final String ID_GENERATOR = "REGION_ID_GENERATOR";
        public static final String ID_SEQUENCE  = "REGION_ID_SEQUENCE";
        public static final String PROCESSED    = "PROCESSED";

    }

    @Id
    @Column(name = Token.ID)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Token.ID_GENERATOR)
    @SequenceGenerator(name = Token.ID_GENERATOR, sequenceName = Token.ID_SEQUENCE, allocationSize = 1)
    private Integer id;

    @Column(name = Token.PROCESSED)
    private boolean processed;

    @Valid
    @NotNull
    @Embedded
    private MinBoxCoordinate min;

    @Valid
    @NotNull
    @Embedded
    private MaxBoxCoordinate max;

    public static RegionEntityBuilder builder() {
        return new RegionEntityBuilder();
    }

    public RegionEntity processed() {
        return setProcessed(true);
    }

    public RegionEntity setProcessed(boolean processed) {
        return setAndReturnThis(processed, x -> this.processed = x);
    }

    private <T> RegionEntity setAndReturnThis(T value, Consumer<T> consumer) {
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

        RegionEntity that = (RegionEntity) o;
        return processed == that.processed &&
                Objects.equal(min, that.min) &&
                Objects.equal(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(processed, min, max);
    }

}
