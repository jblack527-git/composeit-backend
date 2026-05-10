package com.composeit.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Allow API calls on '/api/**' paths
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")  // 3000 = React dev server, 8080 = packaged Electron app
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allow specific methods
                .allowCredentials(true);
    }
}
