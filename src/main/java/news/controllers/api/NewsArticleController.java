package news.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import news.dto.ResponseDto;
import news.entities.NewsArticleEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "News articles controller", description = "Контроллер для взаимодействия со скачанными статьями")
public interface NewsArticleController {
    @GetMapping()
    @Operation(summary = "Вывести все скачанные новостные статьи")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = NewsArticleEntity.class
                                            )
                                    )
                            )
                    }
            )
    })
    List<NewsArticleEntity> getAll();

    @GetMapping("/byNewsSite")
    @Operation(summary = "Получить все новостные статьи по совпадению заголовка сайта")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = NewsArticleEntity.class
                                            )
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found news article by news site",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = ResponseDto.class
                                            )
                                    )
                            )
                    }
            )
    })
    List<NewsArticleEntity> getAllByNewsSite(@RequestParam String newsSite);

    @GetMapping("/byId")
    @Operation(summary = "Получить новостную статью по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = NewsArticleEntity.class
                                            )
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found news article by id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = ResponseDto.class
                                            )
                                    )
                            )
                    }
            )
    })
    NewsArticleEntity getById(@RequestParam Integer id);
}
