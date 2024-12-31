package com.marceloluiz.exchangerate;

import com.marceloluiz.exchangerate.services.ReadmeManagerService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class ExchangeRateApplication implements CommandLineRunner {
	private final ReadmeManagerService readmeManagerService;

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try{
			readmeManagerService.updateReadme();
		}catch (Exception e){
			System.err.println("Error updating README: " + e.getMessage());
			e.printStackTrace();

			System.exit(1);
		}
		System.exit(0);
	}
}
