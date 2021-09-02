package com.electronics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElectronicsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsApplication.class, args);
		
		System.out.println("\nElectronics API Started...\n");
	}

	@Override
	public void run(String... args) {
		
	}

}
