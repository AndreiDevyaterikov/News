package news.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ResponseDto {

    private final HttpStatus httpStatus;
    private final Integer httpCode;
    private final String message;
    private final LocalDateTime timestamp;

    public ResponseDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.httpCode = httpStatus.value();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
