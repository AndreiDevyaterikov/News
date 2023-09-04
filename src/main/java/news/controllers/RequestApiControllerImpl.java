package news.controllers;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import news.controllers.api.RequestApiController;
import news.dto.ResponseDto;
import news.services.LoadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestApiControllerImpl implements RequestApiController {

    private final LoadService loadService;

    @Override
    public ResponseDto getNewsArticles(@Nullable Integer limit, @Nullable Integer start) {
        if (Objects.isNull(limit)) {
            limit = 10;
        }
        if (Objects.isNull(start)) {
            start = 0;
        }
        return loadService.saveNewsArticles(limit, start);
    }
}
