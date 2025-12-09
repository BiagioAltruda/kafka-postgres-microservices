package com.Anagrafe.AdminService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

//@EnableJpaRepositories("com.Anagrafe.entities.*")
@EntityScan(basePackages = {
    "com.Anagrafe.entities",
    "com.Anagrafe.AdminService"
})
@SpringBootApplication
public class AdminServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminServiceApplication.class, args);
  }

}
