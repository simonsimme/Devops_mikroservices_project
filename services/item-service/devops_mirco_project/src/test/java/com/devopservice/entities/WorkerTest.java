package com.devopservice.entities;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class WorkerTest {
    @Test
    void testWorkerBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        Worker worker = Worker.builder()
                .id(id)
                .name("John Doe")
                .role("floor")
                .shifts(new ArrayList<>())
                .shiftAssignments(new ArrayList<>())
                .build();
        assertEquals(id, worker.getId());
        assertEquals("John Doe", worker.getName());
        assertEquals("floor", worker.getRole());
        assertNotNull(worker.getShifts());
        assertNotNull(worker.getShiftAssignments());
    }
}
