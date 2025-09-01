package com.devopservice.auth.dto;
import java.util.UUID;

public record AuthResponse(String accessToken, UUID userId) {}
