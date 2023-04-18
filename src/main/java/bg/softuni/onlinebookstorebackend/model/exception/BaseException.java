package bg.softuni.onlinebookstorebackend.model.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private String message;
    private final Long id;
    public BaseException(Long id) {
        this.id = id;
    }
    protected BaseException setMessage(String message) {
        this.message = message;
        return this;
    }
}
