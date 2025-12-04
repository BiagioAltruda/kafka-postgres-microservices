package com.Anagrafe.entities;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseUser implements Loggable {
  protected String username;
  protected String password;
  protected String clearance;

  public BaseUser(String username, String password, String clearance) {
    this.username = username;
    this.password = password;
    this.clearance = clearance;
  }

  public BaseUser() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getClearance() {
    return clearance;
  }

  public void setClearance(String clearance) {
    this.clearance = clearance;
  }

  @Override
  public String getChangeLog() {
    return "Username: " + username + ". Clearance: " + clearance;
  }
}
