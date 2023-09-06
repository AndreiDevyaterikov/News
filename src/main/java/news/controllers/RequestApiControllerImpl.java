package news.controllers;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.controllers.api.RequestApiController;
import news.dto.ResponseDto;
import news.services.LoadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestApiControllerImpl implements RequestApiController {

    private final LoadService loadService;
    private final HttpServletRequest request;

    @Override
    public ResponseDto getNewsArticles(@Nullable Integer limit, @Nullable Integer start) {

        log.info("===== Request from =====");
        log.info("Request from: {}", request.getRequestURL().toString());
        log.info("Request method: {}", request.getMethod());

        request.getParameterMap()
                .forEach((k, v) -> log.info("Parameter: {} - value: {}", k, v));

        return loadService.saveNewsArticles(limit, start);
    }
}
