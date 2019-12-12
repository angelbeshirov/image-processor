package com.fmi.rest;

import com.fmi.rest.model.Image;
import com.fmi.rest.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;

@SpringBootApplication
public class ApplicationInitializer {

    public static void main(String[] args) {

        SpringApplication.run(ApplicationInitializer.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost");
            }
        };
    }

    @Bean
    public String tesSthm(@Autowired ImageService imageService) {
        Image image = new Image("test.jpg", "asd", LocalDate.now(), 2, 10, 1);
        imageService.saveImage(image);
        Iterable<Image> images = imageService.findAllUploadedBy(2);
            return "";
    }

}
