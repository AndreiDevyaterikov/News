package news.exceptions;


import jakarta.servlet.http.HttpServletResponse;
import news.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NewsExceptionHandler {

    @ExceptionHandler(NotFoundNewsException.class)
    public ResponseDto notFoundException(NotFoundNewsException exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseDto(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
