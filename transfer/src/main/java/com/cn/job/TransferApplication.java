package com.cn.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@SpringBootApplication(exclude = GsonAutoConfiguration.class)
public class TransferApplication {

  public static void main(String[] args) {
    SpringApplication.run(TransferApplication.class, args);
  }
}

