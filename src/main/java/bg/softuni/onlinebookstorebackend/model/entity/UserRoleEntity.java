package bg.softuni.onlinebookstorebackend.model.entity;

import bg.softuni.onlinebookstorebackend.model.enums.UserRoleEnum;
import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class UserRoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum name;

    public UserRoleEntity() {
    }

    public UserRoleEntity(UserRoleEnum name) {
        this.name = name;
    }

    public UserRoleEnum getName() {
        return name;
    }

    public void setName(UserRoleEnum name) {
        this.name = name;
    }
}
