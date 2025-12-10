package com.Anagrafe.DocumentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.kafka.annotation.EnableKafka;

@EntityScan(basePackages = {
    "com.Anagrafe.entities",
    "com.Anagrafe.DocumentService"
})
@SpringBootApplication
@EnableKafka
public class DocumentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocumentServiceApplication.class, args);
  }

}
