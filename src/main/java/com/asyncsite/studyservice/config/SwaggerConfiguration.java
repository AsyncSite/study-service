package com.asyncsite.studyservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI studyServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Study Service API")
                        .description("스터디 제안, 승인, 관리를 위한 마이크로서비스 API")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("AsyncSite Team")
                                .email("contact@asyncsite.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("로컬 개발 서버"),
                        new Server()
                                .url("https://api.asyncsite.com")
                                .description("프로덕션 서버")
                ));
    }
}