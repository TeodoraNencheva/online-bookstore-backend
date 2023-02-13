package bg.softuni.onlinebookstorebackend.model.dto.order;

import bg.softuni.onlinebookstorebackend.model.dto.book.BookAddedToCartDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailsDTO {
    private UUID id;
    private List<BookAddedToCartDTO> items;
    private boolean processed;
    private LocalDateTime createdAt;
}
