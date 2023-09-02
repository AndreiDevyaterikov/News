package news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

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
}
