package com.iwarehouse;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class iWarehouseAPI implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(iWarehouseAPI.class, args);
		
		System.out.println("\n ... iWarehouse API Started ...\n");
	}

	@Override
	public void run(String... args) {
		
	}

}
