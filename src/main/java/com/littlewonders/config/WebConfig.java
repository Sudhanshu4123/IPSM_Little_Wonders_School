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
        // Map /images/uploads/** to the physical directory external-uploads/
        Path uploadDir = Paths.get("external-uploads/");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        
        // Use a more robust file URL format for cross-platform support
        String location = uploadPath.startsWith("/") ? "file:" + uploadPath + "/" : "file:/" + uploadPath + "/";
        
        registry.addResourceHandler("/images/uploads/**")
                .addResourceLocations(location);
    }
}
