package com.Anagrafe.AdminService.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Anagrafe.AdminService.service.AdminDetailsService;
import com.Anagrafe.AdminService.service.JwtService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  AdminDetailsService adminDetailsService;

  @Autowired
  private JwtService jwtService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
      throws Exception { // defines rules for HTTP security

    JwtFilter jwtFilter = new JwtFilter(jwtService, adminDetailsService);

    http
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth // filter who can access resources
            .requestMatchers("/auth/register", "/auth/login").permitAll()
            .requestMatchers("/get-documents").authenticated()
            .requestMatchers("/create-record", "/update-record", "/delete-record").hasRole("ADMIN")
            .anyRequest().authenticated()) // otherwise they need authentication
        .authenticationManager(authenticationManager)
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable()) // disable HTTP basic auth
        .csrf(csrf -> csrf.disable()); // disable CSRF protection for testing
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticiAuthenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

}
