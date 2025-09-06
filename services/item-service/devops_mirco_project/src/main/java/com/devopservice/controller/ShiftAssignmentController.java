package com.devopservice.controller;

import com.devopservice.entities.ShiftAssignment;
import com.devopservice.dto.AssignShiftRequest;
import com.devopservice.entities.Worker;
import com.devopservice.entities.Shift;
import com.devopservice.repositories.ShiftAssignmentRepository;
import com.devopservice.repositories.ShiftRepository;
import com.devopservice.repositories.WorkerRepository;
import com.devopservice.dto.ShiftAssignmentDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/shift-assignments")
public class ShiftAssignmentController {
    
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;

    private static final Logger log = LoggerFactory.getLogger(ShiftAssignmentController.class);
    
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
    public List<ShiftAssignmentDTO> getAssignmentsByWorker(@PathVariable UUID workerId) {
    List<ShiftAssignment> assignments = shiftAssignmentRepository.findByWorkerId(workerId);
    return assignments.stream().map(a -> {
    Shift shift = shiftRepository.findById(a.getShiftId()).orElse(null);
    return new ShiftAssignmentDTO(
        a.getId(),
        a.getShiftId(),
        a.getWorkerId(),
        a.getAssignedAt(),
        shift != null ? shift.getRequiredRole() : null,
        shift != null ? shift.getStartTime() : null,
        shift != null ? shift.getEndTime() : null,
        shift != null ? shift.getDate() : null
    );
}).collect(Collectors.toList());
}
    
    @GetMapping("/shift/{shiftId}")
    public List<ShiftAssignment> getAssignmentsByShift(@PathVariable UUID shiftId) {
        return shiftAssignmentRepository.findByShiftId(shiftId);
    }
    @GetMapping("shift/remove/{shiftId}")
    @Transactional
    public void removeAssignment(@PathVariable UUID shiftId) {
        shiftAssignmentRepository.deleteByShiftId(shiftId);
        shiftRepository.deleteById(shiftId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftAssignment createAssignment(@RequestBody @Valid AssignShiftRequest request) {
        // Verify shift and worker exist
        Optional<Worker> workerOpt  = workerRepository.findById(request.workerId());
        if (!shiftRepository.existsById(request.shiftId())) {
            throw new IllegalArgumentException("Shift not found");
        }
        if (workerOpt.isEmpty()) {
            throw new IllegalArgumentException("Worker not found");
        }
        Worker worker = workerOpt.get();

        ShiftAssignment assignment = ShiftAssignment.builder()
            .id(UUID.randomUUID())
            .shiftId(request.shiftId())
            .workerId(worker.getId())
            .assignedAt(LocalDateTime.now())
            .build();
        
        return shiftAssignmentRepository.save(assignment);
    }

    @PostMapping("/assign")
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftAssignment assignShift(@RequestBody @Valid AssignShiftRequest request) {

        
        Optional<Worker> workerOpt  = workerRepository.findById(request.workerId());
        if (!shiftRepository.existsById(request.shiftId())) {
            throw new IllegalArgumentException("Shift not found");
        }
        if (workerOpt.isEmpty()) {
            throw new IllegalArgumentException("Worker not found");
        }
        Worker worker = workerOpt.get();
        
        

        // Prevent duplicate assignment
        List<ShiftAssignment> existing = shiftAssignmentRepository.findByShiftIdAndWorkerId(request.shiftId(), worker.getId());
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("Assignment already exists for this shift and worker");
        }

        ShiftAssignment assignment = ShiftAssignment.builder()
            .id(UUID.randomUUID())
            .shiftId(request.shiftId())
            .workerId(worker.getId())
            .assignedAt(LocalDateTime.now())
            .build();

        return shiftAssignmentRepository.save(assignment);
}

    @DeleteMapping("/unassign/{shiftId}/{workerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void unassignShift(@PathVariable UUID shiftId, @PathVariable UUID workerId) {
        List<ShiftAssignment> assignments = shiftAssignmentRepository.findByShiftIdAndWorkerId(shiftId, workerId);
        assignments.forEach(a -> shiftAssignmentRepository.deleteById(a.getId()));
    }
}