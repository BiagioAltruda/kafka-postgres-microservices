package com.Anagrafe.entities;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "users")
public class BaseUser implements Loggable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String clearance;

  @Transient
  private String token;

  public BaseUser(Long id, String username, String password, String clearance) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.clearance = clearance;
  }

  public BaseUser(String username, String password, String clearance) {
    this.username = username;
    this.password = password;
    this.clearance = clearance;
  }

  public BaseUser() {
  }

  @Override
  public String getChangeLog() {
    return "Username: " + username + ". Clearance: " + clearance;
  }

  @Override
  public String toString() {
    return "id = " + getId() + " username = " + getUsername() + " password = " + getPassword() + " clearance = "
        + getClearance();
  }
}
