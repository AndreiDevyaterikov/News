package news.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import news.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Request api controller", description = "Контроллер для взаимодействия с ресурсом статей")
public interface RequestApiController {
    @GetMapping()
    @Operation(summary = "Скачать новостные статьи")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "News articles has been loaded"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error occurred during news articles loading"
            )
    })
    ResponseDto getNewsArticles(
            @Parameter(
                    name = "_limit",
                    description = "Ограничение на скачивание новостных статей каждым потоком"
            ) @Nullable @RequestParam Integer limit,
            @Parameter(
                    name = "_start",
                    description = "Количество записей, которое нужно пропустить каждым потоком"
            )
            @Nullable @RequestParam Integer start
    );
}
