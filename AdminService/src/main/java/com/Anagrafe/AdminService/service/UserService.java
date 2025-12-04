package com.Anagrafe.AdminService.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Anagrafe.AdminService.model.AdminUser;
import com.Anagrafe.AdminService.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AdminUser registerUser(String username, String password, String clearance) {
    AdminUser user = new AdminUser();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setClearance(clearance);

    return userRepository.save(user);
  }

  public Optional<String> loginUser(String username, String password) throws UsernameNotFoundException {

    AdminUser user = findUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username Not found"));
    if (passwordEncoder.matches(password, user.getPassword())) {
      return Optional.of(jwtService.generateToken(user));
    } else {
      return Optional.empty();
    }
  }

  public Optional<AdminUser> findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
