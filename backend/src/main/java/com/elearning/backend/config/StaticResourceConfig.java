package com.elearning.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.rmi.registry.Registry;
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Value("${app.storage.local.upload-dir}")
    private String uploadDir;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        String uploadPath= Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();
        System.out.println("Upload Path = " + uploadPath);
        registry.addResourceHandler("/uploads/**").addResourceLocations(uploadPath);
    }
}
