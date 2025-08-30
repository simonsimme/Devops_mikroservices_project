package com.devopservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWorkerRequest(
    @NotBlank String name,
    @NotBlank String role
) {}