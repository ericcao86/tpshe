package com.cmos.tpshe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cmos.tpshe.wh.task.dao")
public class TpsheApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpsheApplication.class, args);
	}
}
