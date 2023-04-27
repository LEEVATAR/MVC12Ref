package com.model2.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Mvc12RefApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mvc12RefApplication.class, args);
	}
	@GetMapping("/")
    public String home() {
        return "main";
    }
	@Configuration
	public class AppConfig {

	    @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                        .allowedOrigins("http://localhost:3000")
	                        .allowedMethods("*")
	                        .maxAge(3600L)
	                        .allowedHeaders("*")
	                        .exposedHeaders("Authorization")
	                        .allowCredentials(true);
	            }
	        };
	        }
	}
}
