package news.services;

import news.dto.NewsArticleDto;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

public interface RequestService {
    List<NewsArticleDto> getArticles(Integer limit, Integer start) throws HttpClientErrorException;
}
