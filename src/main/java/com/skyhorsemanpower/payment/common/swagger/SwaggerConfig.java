package com.skyhorsemanpower.payment.common.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "결제 API 문서", version = "1.0", description = "Documentation API v1.0")
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${openapi.service.url}") String url) {
        SecurityScheme securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER)
            .name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(Arrays.asList(securityRequirement))
            .addServersItem(new Server().url(url));
    }
}
