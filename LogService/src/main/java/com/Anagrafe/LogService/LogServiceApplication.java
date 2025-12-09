package com.Anagrafe.LogService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.kafka.annotation.EnableKafka;

@EntityScan(basePackages = {
    "com.Anagrafe.entities",
    "com.Anagrafe.LogService"
})
@EnableKafka
@SpringBootApplication
public class LogServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogServiceApplication.class, args);
  }

}
