package com.accommodation.accommodation.domain.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowedOriginPatterns("http://183.102.246.88:3000","http://localhost:63342")
                .allowCredentials(true).maxAge(3600);
    }

}
