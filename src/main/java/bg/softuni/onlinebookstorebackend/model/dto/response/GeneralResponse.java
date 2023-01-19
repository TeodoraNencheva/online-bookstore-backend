package bg.softuni.onlinebookstorebackend.model.dto.response;

import java.time.LocalDateTime;

public class GeneralResponse {
    private final LocalDateTime timestamp;
    private final String message;

    public GeneralResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
}
