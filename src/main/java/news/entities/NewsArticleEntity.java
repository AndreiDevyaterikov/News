package news.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news_articles")
public class NewsArticleEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "news_site")
    private String newsSite;

    @Column(name = "published_date")
    private Timestamp publishedDate;

    @Column(name = "article")
    private String article;
}
