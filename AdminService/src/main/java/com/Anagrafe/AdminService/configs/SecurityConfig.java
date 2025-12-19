package com.Anagrafe.AdminService.configs;

import java.util.Arrays;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth // filter who can access resources
            .requestMatchers("/favicon.ico").permitAll()
            .requestMatchers("/index.html").permitAll()
            .requestMatchers("/auth/register", "/auth/login").permitAll()
            .requestMatchers("/home.html").permitAll()
            .requestMatchers("/documentation/get-docs", "/documentation/upload").authenticated()
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
