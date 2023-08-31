package news.services;

import news.dto.NewsArticleDto;

import java.util.List;

public interface RequestService {
    List<NewsArticleDto> getArticles(Integer limit, Integer start);
}
