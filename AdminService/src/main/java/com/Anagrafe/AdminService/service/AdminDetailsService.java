package com.Anagrafe.AdminService.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Anagrafe.AdminService.repository.UserRepository;
import com.Anagrafe.entities.BaseUser;

@Service
public class AdminDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public AdminDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    BaseUser user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));

    return User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .build();
  }

}
