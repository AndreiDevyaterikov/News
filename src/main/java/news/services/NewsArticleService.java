package news.services;

import news.entities.NewsArticleEntity;

import java.util.List;

public interface NewsArticleService {
    List<NewsArticleEntity> getAllNewsArticles();
    List<NewsArticleEntity> getNewsArticlesBySite(String newsSite);
    NewsArticleEntity getNewsArticleById(Integer id);
}
