package com.uade.desarrollo.desarrolloAPP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DesarrolloAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesarrolloAppApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				// Sirve archivos de /uploads/** como recursos estáticos
				registry.addResourceHandler("/uploads/**")
					.addResourceLocations("file:uploads/");
			}
		};
	}
}
