package com.devopservice.entities;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ShiftAssignmentTest {
    @Test
    void testPrePersistSetsAssignedAt() {
        ShiftAssignment assignment = ShiftAssignment.builder()
                .id(UUID.randomUUID())
                .shiftId(UUID.randomUUID())
                .workerId(UUID.randomUUID())
                .build();
        assignment.onCreate();
        assertNotNull(assignment.getAssignedAt());
        assertTrue(assignment.getAssignedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
