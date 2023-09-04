package news.repository;

import news.entities.NewsArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticleEntity, Integer> {
    List<NewsArticleEntity> findAllByNewsSiteLikeIgnoreCase(String newsSite);
}
