package com.devopservice.entities;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {
    @Test
    void testShiftBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(8);
        Shift shift = Shift.builder()
                .id(id)
                .date(date)
                .workerId(UUID.randomUUID())
                .requiredRole("floor")
                .startTime(start)
                .endTime(end)
                .build();
        assertEquals(id, shift.getId());
        assertEquals(date, shift.getDate());
        assertEquals("floor", shift.getRequiredRole());
        assertEquals(start, shift.getStartTime());
        assertEquals(end, shift.getEndTime());
    }
}
