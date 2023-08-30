package news.services.impl;

import news.entities.NewsArticleEntity;
import news.services.NewsArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsArticleServiceImpl implements NewsArticleService {

    @Override
    public List<NewsArticleEntity> getAllNewsArticles() {
        return null;
    }

    @Override
    public List<NewsArticleEntity> getNewsArticlesBySite(String newsSite) {
        return null;
    }

    @Override
    public NewsArticleEntity getNewsArticleById(Integer id) {
        return null;
    }
}
