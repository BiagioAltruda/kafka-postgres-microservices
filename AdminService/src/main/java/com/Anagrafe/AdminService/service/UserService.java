package com.Anagrafe.AdminService.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Anagrafe.AdminService.repository.UserRepository;
import com.Anagrafe.entities.BaseUser;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final Logger logger = LogManager.getLogger("com.Anagrafe.users");

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public BaseUser registerUser(String username, String password, String clearance) {
    BaseUser user = new BaseUser();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setClearance(clearance);

    return userRepository.save(user);
  }

  public Optional<String> loginUser(String username, String password) throws UsernameNotFoundException {
    try {
      BaseUser user = findUserByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("Username Not found"));
      if (passwordEncoder.matches(password, user.getPassword())) {
        logger.info("User found and password matches");
        return Optional.of(jwtService.generateToken(user));
      } else {
        return Optional.empty();
      }
    } catch (UsernameNotFoundException e) {
      logger.error("Username not found", e);
      return Optional.empty();
    }

  }

  public Optional<BaseUser> findUserByUsername(String username) {
    BaseUser user = userRepository.findByUsername(username).orElse(null);
    if (user != null) {
      logger.info("found user with provided username");
      return Optional.of(user);
    }
    logger.error("could not find user with provided username");
    return null;
  }
}
