package com.github.pathfinder.security.database.entity;

import com.github.pathfinder.security.data.user.UserConstant;
import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = UserRefreshTokenEntity.Token.TABLE)
public class UserRefreshTokenEntity {

    public static final class Token {

        public static final String TABLE        = "REFRESH_TOKENS";
        public static final String ID           = "ID";
        public static final String DEVICE       = "DEVICE_NAME";
        public static final String TOKEN        = "TOKEN"; // NOSONAR field naming
        public static final String ISSUED_AT    = "ISSUED_AT";
        public static final String ID_GENERATOR = "REFRESH_TOKEN_ID_GENERATOR";
        public static final String ID_SEQUENCE  = "REFRESH_TOKEN_ID_SEQUENCE";
    }

    @Id
    @Column(name = Token.ID)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Token.ID_GENERATOR)
    @SequenceGenerator(name = Token.ID_GENERATOR, sequenceName = Token.ID_SEQUENCE, allocationSize = 1)
    private Long id;

    @Column(name = Token.DEVICE, length = UserConstant.DEVICE_NAME_MAX_LENGTH, nullable = false)
    private String deviceName;

    @ToString.Exclude
    @Column(name = Token.TOKEN, nullable = false)
    private String token;

    @CreationTimestamp
    @Column(name = Token.ISSUED_AT, nullable = false)
    private LocalDateTime issuedAt;

    @ManyToOne
    @ToString.Exclude
    private UserEntity user;

    public UserRefreshTokenEntity(String deviceName, String token, UserEntity user) {
        this.deviceName = deviceName;
        this.token      = token;
        this.user       = user;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        UserRefreshTokenEntity that = (UserRefreshTokenEntity) object;
        return Objects.equal(deviceName, that.deviceName) &&
                Objects.equal(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceName, token);
    }
}
