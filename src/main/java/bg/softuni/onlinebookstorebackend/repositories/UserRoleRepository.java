package bg.softuni.onlinebookstorebackend.repositories;

import bg.softuni.onlinebookstorebackend.model.entity.UserRoleEntity;
import bg.softuni.onlinebookstorebackend.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByName(UserRoleEnum roleEnum);
}
