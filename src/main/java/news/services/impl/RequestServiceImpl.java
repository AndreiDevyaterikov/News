package news.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.dto.NewsArticleDto;
import news.services.RequestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RestTemplate restTemplate;

    @Value("${request.resource}")
    private String resource;

    @Override
    public List<NewsArticleDto> getArticles(Integer limit, Integer start) throws HttpClientErrorException {
        var uriComponents = UriComponentsBuilder.fromPath(resource)
                .queryParamIfPresent("_limit", Optional.ofNullable(limit))
                .queryParamIfPresent("_start", Optional.ofNullable(start))
                .build();

        var response = restTemplate.getForEntity(uriComponents.toUriString(), NewsArticleDto[].class,
                uriComponents.getQueryParams());

        if (response.getStatusCode().is2xxSuccessful()) {

            var articles = response.getBody();

            if (Objects.isNull(articles)) {
                return Collections.emptyList();
            } else {
                return Arrays.stream(articles).toList();
            }
        } else {
            throw new HttpClientErrorException(response.getStatusCode());
        }
    }
}
