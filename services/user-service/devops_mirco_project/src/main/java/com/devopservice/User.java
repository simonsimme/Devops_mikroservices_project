package com.devopservice;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.OffsetDateTime;

@Entity @Table(name = "Users") // entity Tells JPA that this is a represents a database table
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder //auto generates getters and setters ex getId()

public class User {
  @Id
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;
}
