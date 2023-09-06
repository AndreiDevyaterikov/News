package news.exceptions;

public class NotFoundNewsException extends RuntimeException {
    public NotFoundNewsException(String message) {
        super(message);
    }
}
