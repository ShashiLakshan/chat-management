package com.mychat.mychat.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Chat Storage API",
                version = "v1",
                description = """
            Backend microservice that stores chat sessions, messages, and retrieved context for RAG workflows.
            All endpoints (except health/docs) require headers:
            - X-API-Key: API key
            - X-User-Id: user/tenant identifier
            """,
                contact = @Contact(name = "Platform Team", email = "platform@example.com"),
                license = @License(name = "Apache-2.0")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local"),
                @Server(url = "http://localhost:8080", description = "Docker Compose")
        },
        security = {
                @SecurityRequirement(name = "ApiKeyAuth"),
                @SecurityRequirement(name = "UserIdHeader")
        }
)
@SecurityScheme(
        name = "ApiKeyAuth",
        description = "Provide the API key via `X-API-Key` header.",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-Key"
)
@SecurityScheme(
        name = "UserIdHeader",
        description = "User/Tenant scoping via `X-User-Id` header.",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-User-Id"
)
public class OpenApiConfig {

        @Bean
        public GroupedOpenApi v1GroupedOpenApi() {
                return GroupedOpenApi.builder()
                        .group("v1")
                        .pathsToMatch("/v1/**")
                        .build();
        }
}
