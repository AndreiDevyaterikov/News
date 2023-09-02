package news.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.configs.BlackNewsWords;
import news.dto.NewsArticleDto;
import news.dto.ResponseDto;
import news.entities.NewsArticleEntity;
import news.services.LoadService;
import news.services.NewsArticleService;
import news.services.RequestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoadServiceImpl implements LoadService {

    private final RequestService requestService;
    private final NewsArticleService newsArticleService;
    private final ExecutorService executorService;
    private final BlackNewsWords blackNewsWords;
    private final Map<String, List<NewsArticleDto>> bufferNewsArticles = new HashMap<>();

    @Value("${threads-pool.size}")
    private String threadPoolSize;

    @Value("${buffer-articles.limit}")
    private String bufferLimit;

    @Override
    public ResponseDto saveNewsArticles(Integer limit, Integer start) {

        for (int i = 0; i < Integer.parseInt(threadPoolSize); i++) {

            executorService.execute(() -> {

                var articles = requestService.getArticles(limit, start);

                var filteredAndGroupedArticles = filterAndGroupArticles(articles);

                for (Map.Entry<String, List<NewsArticleDto>> entry : filteredAndGroupedArticles.entrySet()) {

                    var newsSite = entry.getKey();
                    var newsArticles = entry.getValue();

                    synchronized (bufferNewsArticles) {
                        if (bufferNewsArticles.containsKey(newsSite)
                                && bufferNewsArticles.get(newsSite).size() >= Integer.parseInt(bufferLimit)) {

                            var newArticlesEntity = mapToListEntity(newsArticles);

                            newsArticleService.saveAll(newArticlesEntity);
                            bufferNewsArticles.get(newsSite).clear();
                        }
                    }

                    bufferNewsArticles.putIfAbsent(newsSite, new CopyOnWriteArrayList<>());
                    bufferNewsArticles.get(newsSite).addAll(newsArticles);

                }
            });
        }
        executorService.shutdown();
        return new ResponseDto(HttpStatus.CREATED, "News articles has been loaded");
    }

    private Map<String, List<NewsArticleDto>> filterAndGroupArticles(List<NewsArticleDto> newsArticles) {
        return newsArticles
                .stream()
                .filter(article -> !haveBlackWord(article.getTitle()))
                .sorted((a1, a2) -> a2.getPublishedAt().compareTo(a1.getPublishedAt()))
                .collect(Collectors.groupingBy(NewsArticleDto::getNewsSite));
    }

    private boolean haveBlackWord(String title) {
        for (var word : blackNewsWords.getWords()) {
            if (title.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }

    private List<NewsArticleEntity> mapToListEntity(List<NewsArticleDto> newsArticlesDto) {
        return newsArticlesDto
                .stream()
                .map(newsArticle -> NewsArticleEntity.builder()
                        .id(newsArticle.getId())
                        .title(newsArticle.getTitle())
                        .article(newsArticle.getSummary())
                        .newsSite(newsArticle.getNewsSite())
                        .publishedDate(newsArticle.getPublishedAt())
                        .build())
                .toList();
    }
}
