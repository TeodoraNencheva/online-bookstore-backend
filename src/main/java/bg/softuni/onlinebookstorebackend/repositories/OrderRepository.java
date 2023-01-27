package bg.softuni.onlinebookstorebackend.repositories;

import bg.softuni.onlinebookstorebackend.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> getAllByProcessed(boolean processed);

    List<OrderEntity> getAllByOwner_Email(String email);

    Optional<OrderEntity> findById(UUID id);
}
