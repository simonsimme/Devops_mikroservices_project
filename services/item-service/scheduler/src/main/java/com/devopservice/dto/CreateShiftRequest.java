package com.devopservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateShiftRequest(
    @NotNull LocalDate date,
    @NotNull String requiredRole,
    @NotNull LocalDateTime startTime,
    @NotNull LocalDateTime endTime
) {}
