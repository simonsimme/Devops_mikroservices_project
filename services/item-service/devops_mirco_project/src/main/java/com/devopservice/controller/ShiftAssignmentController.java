package com.devopservice.controller;

import com.devopservice.entities.ShiftAssignment;
import com.devopservice.dto.AssignShiftRequest;
import com.devopservice.repositories.ShiftAssignmentRepository;
import com.devopservice.repositories.ShiftRepository;
import com.devopservice.repositories.WorkerRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shift-assignments")
public class ShiftAssignmentController {
    
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    
    public ShiftAssignmentController(
        ShiftAssignmentRepository shiftAssignmentRepository,
        ShiftRepository shiftRepository,
        WorkerRepository workerRepository) {
        this.shiftAssignmentRepository = shiftAssignmentRepository;
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
    }
    
    @GetMapping
    public List<ShiftAssignment> getAllAssignments() {
        return shiftAssignmentRepository.findAll();
    }
    
    @GetMapping("/worker/{workerId}")
    public List<ShiftAssignment> getAssignmentsByWorker(@PathVariable UUID workerId) {
        return shiftAssignmentRepository.findByWorkerId(workerId);
    }
    
    @GetMapping("/shift/{shiftId}")
    public List<ShiftAssignment> getAssignmentsByShift(@PathVariable UUID shiftId) {
        return shiftAssignmentRepository.findByShiftId(shiftId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftAssignment createAssignment(@RequestBody @Valid AssignShiftRequest request) {
        // Verify shift and worker exist
        if (!shiftRepository.existsById(request.shiftId())) {
            throw new IllegalArgumentException("Shift not found");
        }
        if (!workerRepository.existsById(request.workerId())) {
            throw new IllegalArgumentException("Worker not found");
        }
        
        ShiftAssignment assignment = ShiftAssignment.builder()
            .id(UUID.randomUUID())
            .shiftId(request.shiftId())
            .workerId(request.workerId())
            .assignedAt(LocalDateTime.now())
            .build();
        
        return shiftAssignmentRepository.save(assignment);
    }
}