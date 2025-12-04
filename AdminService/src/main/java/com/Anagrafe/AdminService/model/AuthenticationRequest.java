package com.Anagrafe.AdminService.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

  String username;
  String password;
  String clearance;

  public AuthenticationRequest(String username, String password, String clearance) {
    this.username = username;
    this.password = password;
    this.clearance = clearance;
  }
}
