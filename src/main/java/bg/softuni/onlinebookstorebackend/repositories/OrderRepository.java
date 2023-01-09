package bg.softuni.onlinebookstorebackend.repositories;

import bg.softuni.onlinebookstorebackend.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o WHERE o.isProcessed = true")
    List<OrderEntity> getAllProcessed();
    @Query("SELECT o FROM OrderEntity o WHERE o.isProcessed = false")
    List<OrderEntity> getAllUnprocessed();

    List<OrderEntity> getAllByOwner_Email(String email);

    Optional<OrderEntity> findById(UUID id);
}
