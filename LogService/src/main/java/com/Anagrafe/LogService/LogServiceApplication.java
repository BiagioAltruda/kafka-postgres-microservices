package com.Anagrafe.LogService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class LogServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogServiceApplication.class, args);
  }

}
