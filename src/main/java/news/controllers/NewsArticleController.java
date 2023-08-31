package news.controllers;

import lombok.RequiredArgsConstructor;
import news.entities.NewsArticleEntity;
import news.services.NewsArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsArticleController {

    private final NewsArticleService newsArticleService;

    @GetMapping("/all")
    public List<NewsArticleEntity> getAll() {
        return newsArticleService.getAllNewsArticles();
    }

    @GetMapping("/allByNewsSite")
    public List<NewsArticleEntity> getAllByNewsSite(@RequestParam String newsSite) {
        return newsArticleService.getNewsArticlesBySite(newsSite);
    }

    @GetMapping("/getById")
    public NewsArticleEntity getById(@RequestParam Integer id) {
        return newsArticleService.getNewsArticleById(id);
    }
}
