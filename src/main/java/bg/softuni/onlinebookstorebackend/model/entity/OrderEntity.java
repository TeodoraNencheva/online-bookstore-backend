package bg.softuni.onlinebookstorebackend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Getter
    @Setter
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    private UserEntity owner;

    @Getter
    @ElementCollection
    @CollectionTable(name = "orders_items",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "book_id")
    @Column(name = "quantity")
    private Map<BookEntity, Integer> items;

    @Getter
    @Setter
    private boolean processed;

    @Getter
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public OrderEntity(UserEntity owner, Map<BookEntity, Integer> items) {
        this.owner = owner;
        setItems(items);
        this.processed = false;
        this.createdAt = LocalDateTime.now();
    }

    public void setItems(Map<BookEntity, Integer> items) {
        this.items = new LinkedHashMap<>();
        this.items.putAll(items);
    }
}
