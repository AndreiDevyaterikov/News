package news.services.impl;

import lombok.RequiredArgsConstructor;
import news.entities.NewsArticleEntity;
import news.services.NewsArticleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class NewsArticleServiceImpl implements NewsArticleService {

    @Qualifier("taskExecutor")
    private final Executor executors;

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
