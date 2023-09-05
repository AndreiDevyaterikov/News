package news.configs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Value("${request.uri}")
    private String defaultUri;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(getInterceptors());

        UriTemplateHandler uriTemplateHandler = new DefaultUriBuilderFactory(defaultUri);
        restTemplate.setUriTemplateHandler(uriTemplateHandler);
        return restTemplate;
    }

    @Bean
    @Primary
    public ClientHttpRequestInterceptor customClientHttpRequestInterceptor() {
        return new CustomClientHttpRequestInterceptor();
    }

    private List<ClientHttpRequestInterceptor> getInterceptors() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(customClientHttpRequestInterceptor());
        return interceptors;
    }
}
