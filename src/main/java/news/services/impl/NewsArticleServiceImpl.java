package news.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.entities.NewsArticleEntity;
import news.exceptions.NotFoundNewsException;
import news.repository.NewsArticleRepository;
import news.services.NewsArticleService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        var newsArticles = newsArticleRepository.findAllByNewsSiteLikeIgnoreCase("%" + newsSite + "%");
        if (newsArticles.isEmpty()) {
            var message = String.format("Not found articles by %s", newsSite);
            log.warn(message);
            throw new NotFoundNewsException(message);
        } else {
            newsArticles = newsArticles.stream()
                    .sorted(Comparator.comparing(NewsArticleEntity::getNewsSite))
                    .collect(Collectors.toList());
            return newsArticles;
        }
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
    public void save(NewsArticleEntity newsArticleEntity) {
        newsArticleRepository.save(newsArticleEntity);
    }
}
