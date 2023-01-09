package bg.softuni.onlinebookstorebackend.model.error;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
