package com.Anagrafe.AdminService.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Anagrafe.AdminService.model.AdminUser;
import com.Anagrafe.AdminService.model.AuthenticationRequest;
import com.Anagrafe.AdminService.model.AuthenticationResponse;
import com.Anagrafe.AdminService.service.UserService;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public AuthenticationController(
      UserService userService,
      AuthenticationManager authenticationManager,
      KafkaTemplate<String, String> kafkaTemplate) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.kafkaTemplate = kafkaTemplate;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationRequest request) {
    Optional<AdminUser> user = userService.findUserByUsername(request.getUsername());
    if (user.isEmpty()) {
      userService.registerUser(request.getUsername(), request.getPassword(), request.getClearance());
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          request.getUsername(), request.getPassword());
      Authentication auth = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(auth);
      String jwtToken = userService.loginUser(request.getUsername(), request.getPassword()).get();

      // publish message to kafka topic
      kafkaTemplate.send("accounts", request.getUsername());
      return ResponseEntity
          .ok(new AuthenticationResponse(true, "User created successfully", null, jwtToken));
    }
    return ResponseEntity.badRequest().body(new AuthenticationResponse(false, "User already exists", null, null));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest request)
      throws NullPointerException {
    try {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          request.getUsername(), request.getPassword());
      Authentication auth = verifyAuthentication(authenticationToken);

      if (auth == null) {
        throw new NullPointerException("Authentication failed");
      }
      if (auth.isAuthenticated()) {
        String jwtToken = userService.loginUser(request.getUsername(), request.getPassword()).get();
        return ResponseEntity.ok(new AuthenticationResponse(true, "User logged in successfully", null, jwtToken));
      } else {
        return ResponseEntity.badRequest().body(new AuthenticationResponse(false, "User not logged in", null, null));
      }
    } catch (BadCredentialsException e) {
      return ResponseEntity.badRequest()
          .body(new AuthenticationResponse(false, "Invalid username or password", e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new AuthenticationResponse(false, "Unknown error", e.getMessage(), null));
    }
  }

  private Authentication verifyAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
    Authentication auth = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(auth);

    return SecurityContextHolder.getContext().getAuthentication();
  }

  @KafkaListener(topics = "accounts", groupId = "accounts")
  public void userCreated(String msg) {
    System.out.println("Created user with name: " + msg);
  }

}
