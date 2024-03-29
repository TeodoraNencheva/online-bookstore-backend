package bg.softuni.onlinebookstorebackend.model.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class OrderListDTO {
    private UUID id;

    private LocalDateTime createdAt;
}
