package com.devopservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignShiftRequest(
    @NotNull UUID shiftId,
    @NotNull UUID workerId
) {}
