package news.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundNewsException extends RuntimeException {
    private String message;
}
