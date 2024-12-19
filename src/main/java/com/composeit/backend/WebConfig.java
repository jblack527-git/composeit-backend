package com.composeit.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Allow API calls on '/api/**' paths
                .allowedOrigins("http://localhost:3000")  // Allow requests from React (adjust URL for production)
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow specific methods
                .allowCredentials(true);
    }
}
