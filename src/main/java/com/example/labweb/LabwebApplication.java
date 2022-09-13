package com.example.labweb;

import com.example.labweb.configuration.StorageProperties;
import com.example.labweb.service.ArticleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LabwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabwebApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageProperties properties) {
		return (args) -> {
			Files.createDirectories(Paths.get(properties.getLocation()));
		};
	}
}
