package news.controllers;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import news.dto.NewsArticleDto;
import news.services.RequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestApiControllers {

    private final RequestService requestService;

    @GetMapping()
    public List<NewsArticleDto> getNewsArticles(@Nullable @RequestParam Integer limit,
                                                @Nullable @RequestParam Integer start
    ) {
        return requestService.getArticles(limit, start);
    }
}
