package news.services.impl;

import lombok.RequiredArgsConstructor;
import news.dto.ResponseDto;
import news.entities.NewsArticleEntity;
import news.services.LoadService;
import news.services.NewsArticleService;
import news.services.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoadServiceImpl implements LoadService {

    private final RequestService requestService;
    private final NewsArticleService newsArticleService;

    @Override
    public ResponseDto saveNewsArticles(Integer limit, Integer start) {
        var newsArticlesDto = requestService.getArticles(limit, start);
        var newArticlesEntities = newsArticlesDto
                .stream()
                .map(newsArticleDto -> NewsArticleEntity.builder()
                        .id(newsArticleDto.getId())
                        .title(newsArticleDto.getTitle())
                        .newsSite(newsArticleDto.getNewsSite())
                        .publishedDate(newsArticleDto.getPublishedAt())
                        .article(newsArticleDto.getSummary())
                        .build())
                .collect(Collectors.toList());
        newsArticleService.saveAll(newArticlesEntities);
        return new ResponseDto(HttpStatus.CREATED, "News articles has been loaded");
    }
}
