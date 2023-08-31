package news.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.entities.NewsArticleEntity;
import news.exceptions.NotFoundNewsException;
import news.repository.NewsArticleRepository;
import news.services.NewsArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsArticleServiceImpl implements NewsArticleService {

    private final NewsArticleRepository newsArticleRepository;

    @Override
    public List<NewsArticleEntity> getAllNewsArticles() {
        return newsArticleRepository.findAll();
    }

    @Override
    public List<NewsArticleEntity> getNewsArticlesBySite(String newsSite) {
        return newsArticleRepository.findAllByNewsSite(newsSite);
    }

    @Override
    public NewsArticleEntity getNewsArticleById(Integer id) {
        return newsArticleRepository.findById(id).orElseThrow(() -> {
            var message = String.format("Not found news article by id %d", id);
            log.warn(message);
            return new NotFoundNewsException(message);
        });
    }

    @Override
    public void saveAll(List<NewsArticleEntity> newsArticleEntities) {
        newsArticleRepository.saveAll(newsArticleEntities);
    }
}
