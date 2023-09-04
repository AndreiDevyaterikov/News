package news.controllers;

import lombok.RequiredArgsConstructor;
import news.controllers.api.NewsArticleController;
import news.entities.NewsArticleEntity;
import news.services.NewsArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsArticleControllerImpl implements NewsArticleController {

    private final NewsArticleService newsArticleService;

    @Override
    public List<NewsArticleEntity> getAll() {
        return newsArticleService.getAllNewsArticles();
    }

    @Override
    public List<NewsArticleEntity> getAllByNewsSite(@RequestParam String newsSite) {
        return newsArticleService.getNewsArticlesBySite(newsSite);
    }

    @Override
    public NewsArticleEntity getById(@RequestParam Integer id) {
        return newsArticleService.getNewsArticleById(id);
    }
}
