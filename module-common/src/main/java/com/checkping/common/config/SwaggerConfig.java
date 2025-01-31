package com.checkping.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 서버"),
                @Server(url = "https://test.flowssync.com", description = "테스트 서버"),
                @Server(url = "https://api.flowssync.com", description = "개발 서버"),
                @Server(url = "https://prod.flowssync.com", description = "운영 서버"),
        })
@Configuration
public class SwaggerConfig {

    private final ObjectMapper objectMapper;

    public SwaggerConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void initialize() {
        TypeNameResolver innerClassAwareTypeNameResolver = new TypeNameResolver() {
            @Override
            public String getNameOfClass(Class<?> cls) {
                /*
                    cls.getName() -> com.checkping.dto.OrganizationUpdate$Response
                    subString(lastIndexOf(".") + 1) -> OrganizationUpdate$Response
                    replace("$", ".") -> OrganizationUpdate.Response
                 */

                return cls.getName()
                    .substring(cls.getName().lastIndexOf(".") + 1)
                    .replace("$", ".");
            }
        };

        ModelConverters.getInstance()
            .addConverter(new ModelResolver(objectMapper, innerClassAwareTypeNameResolver));
    }

    @Bean
    public OpenAPI openAPI() {
        /*
        JWT 인증 스키마 설정
        API 문서에 보안 설정 추가되어 API 요청시 JWT 토큰을 헤더에 포함시키도록 할 수 있다.
         */
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));


        // Components 객체 - 보안 스키마, 파라미터, 응답 등 정의
        return new OpenAPI()
                .components(new Components())
                .components(components)
                .addSecurityItem(securityRequirement)
                .info(apiInfo());
    }

    // API문서의 제목, 설명, 버전 등의 기본 정보 설정
    private Info apiInfo() {
        return new Info()
                .title("FlowSync API")
                .description("FlowSync Swagger API Document")
                .version("1.0");
    }
}