package com.github.pathfinder.security.database.entity;

import com.github.pathfinder.security.data.user.UserConstant;
import com.google.common.base.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
        public static final String ID_GENERATOR = "USER_ID_GENERATOR";
        public static final String ID_SEQUENCE  = "USER_ID_SEQUENCE";
    }

    public static final class Constant {

        public static final int NAME_MAX_LENGTH     = UserConstant.NAME_MAX_LENGTH;
        public static final int PASSWORD_MAX_LENGTH = 100;
    }

    @Id
    @Column(name = Token.ID)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Token.ID_GENERATOR)
    @SequenceGenerator(name = Token.ID_GENERATOR, sequenceName = Token.ID_SEQUENCE, allocationSize = 1)
    private Long id;

    @Column(name = Token.NAME, nullable = false, unique = true, length = Constant.NAME_MAX_LENGTH)
    private String name;

    @ToString.Exclude
    @Column(name = Token.PASSWORD, nullable = false, length = Constant.PASSWORD_MAX_LENGTH)
    private String password;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserRolesEntity roles;

    public UserEntity(String name, String password, UserRolesEntity roles) {
        this.name     = name;
        this.password = password;
        this.roles    = roles;
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
