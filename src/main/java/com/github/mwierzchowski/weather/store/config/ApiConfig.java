package com.github.mwierzchowski.weather.store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    OpenAPI openApi(final Environment env) {
        var appInfo = new Info()
                .title(env.getProperty("info.app.name"))
                .description(env.getProperty("info.app.description"));
        return new OpenAPI().info(appInfo);
    }
}
