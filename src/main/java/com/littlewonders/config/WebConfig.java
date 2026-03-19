package com.littlewonders.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /images/uploads/** to the physical directory src/main/resources/static/images/uploads/
        // In local development, this helps pick up newly uploaded files immediately
        Path uploadDir = Paths.get("src/main/resources/static/images/uploads/");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/images/uploads/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}
