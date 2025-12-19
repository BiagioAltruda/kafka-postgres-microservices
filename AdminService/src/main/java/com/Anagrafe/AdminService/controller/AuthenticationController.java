package com.Anagrafe.AdminService.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Anagrafe.AdminService.model.AuthenticationRequest;
import com.Anagrafe.AdminService.model.AuthenticationResponse;
import com.Anagrafe.AdminService.service.JwtService;
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
  private final JwtService jwtService;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTemplate<String, ChangeLog> kafkaLogTemplate;
  private final Logger logger = LogManager.getLogger("Authentication_Logger");
  private final Logger userLogger = LogManager.getLogger("com.Anagrafe.users");

  public AuthenticationController(
      UserService userService,
      AuthenticationManager authenticationManager,
      KafkaTemplate<String, String> kafkaTemplate,
      KafkaTemplate<String, ChangeLog> kafkaLogTemplate,
      JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaLogTemplate = kafkaLogTemplate;
    this.jwtService = jwtService;
    logger.info("Authentication controller created");
    logger.warn("Authentication controller created");
    logger.debug("Authentication controller created");
    logger.error("Authentication controller created");

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
      userLogger.info("User created:  username={}, jwt_token={}", request.getUsername(), jwtToken);

      return ResponseEntity
          .ok(new AuthenticationResponse(true, "User created successfully", null, jwtToken));
    }
    userLogger.error("User already exists:  tried_username={}", request.getUsername());
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
        userLogger.error("Authentication failed:  tried_username={}, tried_password={}", request.getUsername(),
            request.getPassword());
        throw new NullPointerException("Authentication failed");
      }
      if (auth.isAuthenticated()) {
        String jwtToken = userService.loginUser(request.getUsername(), request.getPassword()).get();
        userLogger.info("User logged in:  username={}, jwt_token={}", request.getUsername(), jwtToken);
        return ResponseEntity.ok().header("Authorization", "Bearer " + jwtToken)
            .body(new AuthenticationResponse(true, "User logged in", null, jwtToken));
      } else {

        userLogger.error("User not logged in:  tried_username={}, tried_password={}", request.getUsername(),
            request.getPassword());
        return ResponseEntity.badRequest().body(new AuthenticationResponse(false, "User not logged in", null, null));
      }
    } catch (BadCredentialsException e) {
      userLogger.error("User not logged in:  tried_username={}, tried_password={}", request.getUsername(),
          request.getPassword(), e);
      return ResponseEntity.badRequest()
          .body(new AuthenticationResponse(false, "Invalid username or password", e.getMessage(), null));
    } catch (Exception e) {
      userLogger.error("User not logged in:  tried_username={}, tried_password={}", request.getUsername(),
          request.getPassword(), e);
      return ResponseEntity.badRequest().body(new AuthenticationResponse(false, "Unknown error", e.getMessage(), null));
    }
  }

  @Deprecated
  @GetMapping("/from-token")
  public ResponseEntity<Optional<Long>> getUserFromToken(@RequestHeader String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    try {
      BaseUser user = userService.findUserByUsername(username).orElseThrow();
      return ResponseEntity.ok(Optional.of(user.getId()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Optional.empty());
    }
  }

  private Authentication verifyAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
    Authentication auth = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(auth);

    return SecurityContextHolder.getContext().getAuthentication();
  }

  private ChangeLog createChangeLog(String topic, String message, BaseUser user) {

    logger.info(user);

    ChangeLog log = new ChangeLog(
        EventType.fromString(topic), Optional.of(user),
        message, LocalDateTime.now());
    logger.info("FROM CONTROLLER");
    logger.info(log.toString());

    return log;
  }

}
