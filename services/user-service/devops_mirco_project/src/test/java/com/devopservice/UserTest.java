package com.devopservice;


import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithBuilder() {
        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        String passwordHash = "hashedPassword";
        OffsetDateTime now = OffsetDateTime.now();

        User user = User.builder()
                .id(id)
                .email(email)
                .passwordHash(passwordHash)
                .createdAt(now)
                .build();

        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(now, user.getCreatedAt());
    }
}
