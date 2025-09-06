package com.devopservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import java.time.LocalDate;

public record ShiftAssignmentDTO(
    UUID id,
    UUID shiftId,
    UUID workerId,
    LocalDateTime assignedAt,
    String requiredRole,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDate date
) {}