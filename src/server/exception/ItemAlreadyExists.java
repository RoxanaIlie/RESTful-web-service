package server.exception;

public class ItemAlreadyExists extends RuntimeException {
    private String message;

    public ItemAlreadyExists(String message) {
        super(message);
        this.message = message;
    }
}
