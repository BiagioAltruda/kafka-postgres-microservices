package com.Anagrafe.AdminService.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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

import com.Anagrafe.AdminService.model.AuthenticationRequest;
import com.Anagrafe.AdminService.model.AuthenticationResponse;
import com.Anagrafe.AdminService.service.UserService;
import com.Anagrafe.entities.BaseUser;
import com.Anagrafe.entities.ChangeLog;
import com.Anagrafe.entities.enums.EventType;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTemplate<String, ChangeLog> kafkaLogTemplate;

  public AuthenticationController(
      UserService userService,
      AuthenticationManager authenticationManager,
      KafkaTemplate<String, String> kafkaTemplate,
      KafkaTemplate<String, ChangeLog> kafkaLogTemplate) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaLogTemplate = kafkaLogTemplate;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationRequest request) {
    Optional<BaseUser> user = userService.findUserByUsername(request.getUsername());
    if (user.isEmpty()) {
      user = Optional
          .of(userService.registerUser(request.getUsername(), request.getPassword(), request.getClearance()));
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          request.getUsername(), request.getPassword());
      Authentication auth = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(auth);
      String jwtToken = userService.loginUser(request.getUsername(), request.getPassword()).get();
      ChangeLog log = createChangeLog("account-creation", "User created", user.get());
      // publish message to kafka topic
      kafkaLogTemplate.send("account-creation", log);

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

  private ChangeLog createChangeLog(String topic, String message, BaseUser user) {

    System.out.println(user.toString());

    ChangeLog log = new ChangeLog(
        EventType.fromString(topic), Optional.of(user),
        message, LocalDateTime.now());
    System.out.println("FROM CONTROLLER");
    System.out.println(log.toString());

    return log;
  }

}
