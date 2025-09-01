package com.devopservice.auth;


import com.devopservice.auth.dto.*;
import com.devopservice.User;
import com.devopservice.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
    this.users = users; this.encoder = encoder; this.jwt = jwt;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponse register(@RequestBody @Valid RegisterRequest req) {
    if (users.existsByEmail(req.email())) {
      throw new IllegalArgumentException("Email already in use");
    }
    var u = User.builder()
      .id(UUID.randomUUID())
      .email(req.email().toLowerCase())
      .passwordHash(encoder.encode(req.password()))
      .createdAt(OffsetDateTime.now())
      .build();
    users.save(u);
    return new AuthResponse(jwt.generate(u.getId().toString()), u.getId());
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody @Valid LoginRequest req) {
    var u = users.findByEmail(req.email().toLowerCase())
      .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
      
    if (!encoder.matches(req.password(), u.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    return new AuthResponse(jwt.generate(u.getId().toString()), u.getId());
  }

  @GetMapping("/users") //ONLY IN TESTING REMOVE ON PRODUCTION
  public java.util.List<User> getAllUsers() {
      return users.findAll();
  }

  @GetMapping("/profile")
public String getProfile() {
    // This endpoint requires authentication
    return "This is a protected endpoint! You are authenticated.";
}

}
