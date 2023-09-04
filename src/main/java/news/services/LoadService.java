package news.services;

import news.dto.ResponseDto;

public interface LoadService {
    ResponseDto saveNewsArticles(Integer limit, Integer start);
}
