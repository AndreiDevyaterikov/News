package news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsArticleDto {
    private Integer id;
    private String title;
    private String url;
    private String imageUrl;
    private String newsSite;
    private String summary;
    private Timestamp publishedAt;
    private Timestamp updatedAt;
    private Boolean featured;
    private List<LaunchDto> launches;
    private List<EventDto> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsArticleDto that = (NewsArticleDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(newsSite, that.newsSite)
                && Objects.equals(summary, that.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, newsSite, summary);
    }
}
