package com.github.pathfinder.security.database.entity;


import com.google.common.base.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = UserRolesEntity.Token.TABLE)
public class UserRolesEntity {

    public static final class Token {

        public static final String TABLE = "USERS";
        public static final String ID    = "ID";
        public static final String ROLE  = "ROLE";
    }

    @Id
    @Column(name = UserRolesEntity.Token.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity user;

    @Column(name = UserRolesEntity.Token.ROLE)
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRolesEntity that = (UserRolesEntity) o;
        return Objects.equal(user, that.user) &&
                Objects.equal(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user, role);
    }
}
