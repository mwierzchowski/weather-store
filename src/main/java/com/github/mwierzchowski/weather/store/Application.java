package com.github.mwierzchowski.weather.store;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class Application implements WebMvcConfigurer {
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

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
