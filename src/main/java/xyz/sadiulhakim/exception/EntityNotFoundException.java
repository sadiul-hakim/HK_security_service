package xyz.sadiulhakim.exception;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5228575158133634057L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
