package bg.softuni.onlinebookstorebackend.model.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order not found")
public class OrderNotFoundException extends RuntimeException {
    private UUID id;

    public OrderNotFoundException(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
