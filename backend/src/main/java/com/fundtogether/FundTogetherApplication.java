package com.fundtogether;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fundtogether.mapper")
public class FundTogetherApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundTogetherApplication.class, args);
	}

}
