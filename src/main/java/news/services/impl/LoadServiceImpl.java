package news.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.configs.BlackNewsWords;
import news.configs.LoaderConfig;
import news.dto.NewsArticleDto;
import news.dto.ResponseDto;
import news.entities.NewsArticleEntity;
import news.services.LoadService;
import news.services.NewsArticleService;
import news.services.RequestService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoadServiceImpl implements LoadService {

    private final RequestService requestService;
    private final NewsArticleService newsArticleService;

    private final BlackNewsWords blackNewsWords;
    private final LoaderConfig loaderConfig;
    private final Map<String, List<NewsArticleDto>> bufferNewsArticles = new HashMap<>();

    @Qualifier("taskPoolConfig")
    private final ThreadPoolTaskExecutor taskExecutor;

    @Override
    public ResponseDto saveNewsArticles(Integer limit, Integer start) {

        var bufferLimit = loaderConfig.getBufferLimit();

        AtomicInteger downloadLimit = new AtomicInteger(loaderConfig.getDownloadLimit());

        int threadCount = taskExecutor.getCorePoolSize();
        CountDownLatch threadLatch = new CountDownLatch(threadCount);

        for (int indexThread = 0; indexThread < threadCount; indexThread++) {
            taskExecutor.execute(() -> {
                log.info("{} has been started", Thread.currentThread().getName());
                try {
                    var downloadedArticles = requestService.getArticles(limit, start);

                    synchronized (downloadLimit) {
                        if (downloadLimit.get() == 0) {
                            log.info("{} interrupted. Reason: no available limit.", Thread.currentThread().getName());
                            Thread.currentThread().interrupt();
                            return;
                        } else {
                            if (downloadLimit.get() > downloadedArticles.size()) {
                                downloadLimit.set(downloadLimit.get() - downloadedArticles.size());
                            } else {
                                downloadedArticles = downloadedArticles
                                        .subList(0, downloadLimit.get())
                                        .stream()
                                        .toList();
                                downloadLimit.set(0);
                            }
                        }
                    }

                    var filteredArticles = filterAndGroupArticles(downloadedArticles);
                    filteredArticles.forEach((newsSite, articles) -> {
                        synchronized (bufferNewsArticles) {
                            if (bufferNewsArticles.containsKey(newsSite)
                                    && bufferNewsArticles.get(newsSite).size() >= bufferLimit) {

                                log.info("Buffer limit has been exceeded for site {}", newsSite);

                                var newArticlesEntity = mapToListEntity(articles);
                                newsArticleService.saveAll(newArticlesEntity);

                                log.info("Saved all articles for site {}", newsSite);

                                bufferNewsArticles.get(newsSite).clear();
                            }
                        }
                        bufferNewsArticles.putIfAbsent(newsSite, new ArrayList<>());
                        bufferNewsArticles.get(newsSite).addAll(articles);
                    });
                } finally {
                    threadLatch.countDown();
                    log.info("{} has been finished", Thread.currentThread().getName());
                }
            });
        }

        try {
            threadLatch.await();
            saveAndClearBufferNewsArticles();
            return new ResponseDto(HttpStatus.CREATED, "News articles has been loaded");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage());
            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during news articles loading");
        }
    }

    private void saveAndClearBufferNewsArticles() {
        synchronized (bufferNewsArticles) {
            bufferNewsArticles.forEach((newsSite, newsArticles) -> {
                var newArticlesEntity = mapToListEntity(newsArticles);
                newsArticleService.saveAll(newArticlesEntity);
            });
            bufferNewsArticles.clear();
            log.info("Buffer has been cleared");
        }
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
