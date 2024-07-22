package com.book.backend.global;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition(info = @Info(title = "북토크 서버 API", description = "설명", version = "1.0")) // 상단 제목 커스텀
@Configuration
public class SwaggerConfig { }
