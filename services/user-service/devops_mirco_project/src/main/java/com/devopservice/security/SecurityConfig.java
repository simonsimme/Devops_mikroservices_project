package com.devopservice.security;


import com.devopservice.auth.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService) throws Exception {
    http.csrf(csrf -> csrf.disable())
       .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
       .authorizeHttpRequests(auth -> auth
         .requestMatchers("/","/actuator/health", "/v3/api-docs/**", "/swagger-ui/**" , 
                   "/api/auth/register", "/api/auth/login", "/api/auth/users").permitAll()
         .anyRequest().authenticated()
       )
       .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
