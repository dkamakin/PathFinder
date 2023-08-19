package com.github.pathfinder.security.database.entity;

import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = UserEntity.Token.TABLE)
public class UserEntity {

    public static final class Token {
        public static final String TABLE        = "USERS";
        public static final String ID           = "ID";
        public static final String NAME         = "NAME";
        public static final String PASSWORD     = "PASSWORD";
        public static final String ID_GENERATOR = "ID_GENERATOR";
        public static final String ID_SEQUENCE  = "ID_SEQUENCE";
    }

    @Id
    @Column(name = Token.ID)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Token.ID_GENERATOR)
    @SequenceGenerator(name = Token.ID_GENERATOR, sequenceName = Token.ID_SEQUENCE, allocationSize = 1)
    private Long id;

    @Column(name = Token.NAME, nullable = false, unique = true)
    private String name;

    @ToString.Exclude
    @Column(name = Token.PASSWORD, nullable = false)
    private String password;

    public UserEntity(UserDetails userDetails) {
        this.name = userDetails.getUsername();
        this.password = userDetails.getPassword();
    }

    public UserEntity setPassword(String password) {
        return setAndReturnThis(password, x -> this.password = password);
    }

    private <T> UserEntity setAndReturnThis(T value, Consumer<T> valueConsumer) {
        valueConsumer.accept(value);
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
        UserEntity that = (UserEntity) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, password);
    }
}
