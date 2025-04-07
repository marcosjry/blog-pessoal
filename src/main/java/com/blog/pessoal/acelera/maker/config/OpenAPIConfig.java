package com.blog.pessoal.acelera.maker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API do Blog Pessoal")
                        .version("v1")
                        .description("Esta API utiliza autenticação via **JWT Bearer Token**. \n" +
                                "\n" +
                                "        ▶Para testar as rotas protegidas:\n" +
                                "        1. Use a rota de ( `/api/usuarios` POST.METHOD ) para criar um usuário.\n" +
                                "        2. Use a rota de login ( `/api/usuarios/login` ) para obter o token.\n" +
                                "        3. Clique no botão \"Authorize\" no Swagger e insira `Bearer SEU_TOKEN`.\n" +
                                "\n" +
                                "        Algumas rotas como criação de postagem, temas etc. exigem token."))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)));
    }
}
