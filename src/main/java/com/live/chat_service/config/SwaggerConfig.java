package com.live.chat_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "live-chat-backend";

    @Bean
    public OpenAPI customOpenAPI() {
        License license = new License();
        license.setName(SECURITY_SCHEME_NAME);
        license.setUrl("liveChat");

        final String apiTitle = String.format("%s API", StringUtils.capitalize(SECURITY_SCHEME_NAME));
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(
                        new Components()
                                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .description("Access token")
                                                .bearerFormat("JWT")
                                                .in(SecurityScheme.In.HEADER)
                                )
                )
                .info(new Info().title(apiTitle).version("1.0").description(SECURITY_SCHEME_NAME).termsOfService("liveChat").license(license));
    }

}