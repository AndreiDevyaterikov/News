package news.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.config.BlackNewsWords;
import news.config.LoaderConfig;
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
import org.springframework.web.client.HttpClientErrorException;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private final Map<String, List<NewsArticleDto>> bufferNewsArticles = new ConcurrentHashMap<>();

    @Qualifier("taskPoolConfig")
    private final ThreadPoolTaskExecutor taskExecutor;

    @Override
    public ResponseDto saveNewsArticles(Integer limit, Integer start) {

        var bufferLimit = loaderConfig.getBufferLimit();

        AtomicInteger downloadLimit = new AtomicInteger(loaderConfig.getDownloadLimit());

        AtomicInteger currentDownloadLimit = new AtomicInteger(0);

        int threadCount = taskExecutor.getCorePoolSize();
        CountDownLatch threadLatch = new CountDownLatch(threadCount);


        for (int indexThread = 0; indexThread < threadCount; indexThread++) {

            int currentThreadStart = indexThread * limit + start;

            taskExecutor.execute(() -> {

                log.info("{} has been started", Thread.currentThread().getName());
                try {

                    var downloadedArticles = requestService.getArticles(limit, currentThreadStart);

                    currentDownloadLimit.addAndGet(downloadedArticles.size());

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

                        if (bufferNewsArticles.containsKey(newsSite)
                                && bufferNewsArticles.get(newsSite).size() >= bufferLimit) {

                            var uniqArticles = new HashSet<>(articles);
                            uniqArticles.addAll(bufferNewsArticles.get(newsSite));

                            uniqArticles.forEach(article -> {

                                var articleEntity = NewsArticleEntity.builder()
                                        .id(article.getId())
                                        .title(article.getTitle())
                                        .newsSite(article.getNewsSite())
                                        .article(article.getSummary())
                                        .publishedDate(article.getPublishedAt())
                                        .build();
                                newsArticleService.save(articleEntity);

                            });

                            bufferNewsArticles.get(newsSite).clear();
                        } else {
                            bufferNewsArticles.putIfAbsent(newsSite, new CopyOnWriteArrayList<>());
                            bufferNewsArticles.get(newsSite).addAll(articles);
                        }
                    });
                } catch (HttpClientErrorException e) {
                    Thread.currentThread().interrupt();
                    log.info("{} interrupted. Reason: error on request. Http status - {}",
                            Thread.currentThread().getName(), e.getStatusCode());
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
            throw new RuntimeException("");
        }
    }

    private void saveAndClearBufferNewsArticles() {
        bufferNewsArticles.forEach((newsSite, newsArticles) -> newsArticles.forEach(article -> {
            var articleEntity = NewsArticleEntity.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .newsSite(article.getNewsSite())
                    .article(article.getSummary())
                    .publishedDate(article.getPublishedAt())
                    .build();
            newsArticleService.save(articleEntity);
        }));
        bufferNewsArticles.clear();
        log.info("Buffer has been cleared");
    }

    private Map<String, List<NewsArticleDto>> filterAndGroupArticles(List<NewsArticleDto> newsArticles) {
        return newsArticles
                .stream()
                .filter(article -> !haveBlackWord(article.getTitle()))
                .sorted(Comparator.comparing(NewsArticleDto::getPublishedAt))
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
}
