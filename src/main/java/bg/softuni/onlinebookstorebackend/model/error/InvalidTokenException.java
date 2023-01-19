package bg.softuni.onlinebookstorebackend.model.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
