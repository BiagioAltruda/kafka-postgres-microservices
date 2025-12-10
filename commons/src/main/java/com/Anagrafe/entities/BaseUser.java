package com.Anagrafe.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class BaseUser extends Persistable implements Loggable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;
  private String username;
  private String password;
  private String clearance;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
  private List<Document> documents;

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
