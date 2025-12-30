package com.rakbow.kureakurusu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rakbow.kureakurusu.dao")
public class KureaKurusuApp {

	public static void main(String[] args) {
		SpringApplication.run(KureaKurusuApp.class, args);
	}

}
