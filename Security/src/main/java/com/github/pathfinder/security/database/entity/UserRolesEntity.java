package com.github.pathfinder.security.database.entity;


import com.github.pathfinder.security.data.user.UserConstant;
import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;

@Getter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = UserRolesEntity.Token.TABLE)
public class UserRolesEntity {

    @UtilityClass
    public static class Token {

        public static final String TABLE               = "USERS_ROLES";
        public static final String ID                  = "ID";
        public static final String ROLE                = "ROLE";
        public static final String USER_TO_ROLE_COLUMN = "USER_ID";
        public static final String ID_GENERATOR        = "ROLE_ID_GENERATOR";
        public static final String ID_SEQUENCE         = "ROLE_ID_SEQUENCE";
    }

    @Id
    @Column(name = UserRolesEntity.Token.ID)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = UserRolesEntity.Token.ID_GENERATOR)
    @SequenceGenerator(name = UserRolesEntity.Token.ID_GENERATOR, sequenceName = UserRolesEntity.Token.ID_SEQUENCE, allocationSize = 1)
    private Long id;

    @Column(name = UserRolesEntity.Token.ROLE, length = UserConstant.ROLE_MAX_LENGTH, nullable = false)
    private String role;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = Token.USER_TO_ROLE_COLUMN)
    private UserEntity user;

    public UserRolesEntity(String role) {
        this.role = role;
    }

    public UserRolesEntity setUser(UserEntity entity) {
        return setAndReturnThis(entity, x -> this.user = x);
    }

    private <T> UserRolesEntity setAndReturnThis(T object, Consumer<T> setter) {
        setter.accept(object);
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        UserRolesEntity that = (UserRolesEntity) object;
        return Objects.equal(id, that.id) &&
                Objects.equal(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, role);
    }
}
