package edu.nus.java_ca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JavaCaApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(JavaCaApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {

		};
	}
}
