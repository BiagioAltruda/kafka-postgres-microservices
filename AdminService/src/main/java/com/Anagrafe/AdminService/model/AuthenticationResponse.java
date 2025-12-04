package com.Anagrafe.AdminService.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {

  boolean success;
  String message;
  String error;
  String jwtToken;

  public AuthenticationResponse(boolean success, String message, String error, String jwt) {
    this.success = success;
    this.message = message;
    this.error = error;
    this.jwtToken = jwt;
  }
}
