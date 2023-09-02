package news.controllers;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import news.dto.ResponseDto;
import news.services.LoadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestApiController {

    private final LoadService loadService;

    @GetMapping()
    public ResponseDto getNewsArticles(@Nullable @RequestParam Integer limit,
                                       @Nullable @RequestParam Integer start) {
        if(Objects.isNull(limit)) {
            limit = 10;
        }
        if(Objects.isNull(start)) {
            start = 0;
        }
        return loadService.saveNewsArticles(limit, start);
    }
}
