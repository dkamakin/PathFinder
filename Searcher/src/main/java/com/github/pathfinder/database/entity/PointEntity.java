package com.github.pathfinder.database.entity;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Node("Point")
@NoArgsConstructor
public class PointEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "CONNECTION")
    private Set<PointEntity> connection;

    private Integer  altitude;
    private Integer  longitude;
    private Integer  latitude;
    private LandType landType;

    public PointEntity(Integer altitude, Integer longitude, Integer latitude, LandType landType) {
        this.altitude = altitude;
        this.longitude = longitude;
        this.latitude = latitude;
        this.landType = landType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PointEntity point = (PointEntity) o;

        return new EqualsBuilder().append(altitude, point.altitude)
                .append(longitude, point.longitude)
                .append(latitude, point.latitude)
                .append(landType, point.landType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(altitude)
                .append(longitude).append(latitude)
                .append(landType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("altitude", altitude)
                .append("longitude", longitude)
                .append("latitude", latitude)
                .append("landType", landType)
                .toString();
    }
}
