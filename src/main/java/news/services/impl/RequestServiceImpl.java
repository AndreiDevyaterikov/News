package news.services.impl;

import lombok.RequiredArgsConstructor;
import news.dto.NewsArticleDto;
import news.services.RequestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final WebClient webClient;

    @Value("${request.resource}")
    private String resource;

    @Override
    public List<NewsArticleDto> getArticles(Integer limit, Integer start) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(resource)
                        .queryParamIfPresent("_limit", Optional.ofNullable(limit))
                        .queryParamIfPresent("_start", Optional.ofNullable(start))
                        .build()
                )
                .retrieve()
                .bodyToFlux(NewsArticleDto.class)
                .collectList()
                .block();
    }
}
