package com.devopservice.controller;

import com.devopservice.entities.Shift;
import com.devopservice.dto.CreateShiftRequest;
import com.devopservice.repositories.ShiftRepository;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    
    private final ShiftRepository shiftRepository;
    
    public ShiftController(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }
    
    @GetMapping
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Shift getShiftById(@PathVariable UUID id) {
        return shiftRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
    }
    
    @GetMapping("/date/{date}")
    public List<Shift> getShiftsByDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return shiftRepository.findByDate(date);
    }
    
    @GetMapping("/unassigned")
    public List<Shift> getUnassignedShifts() {
        return shiftRepository.findByWorkerIdIsNull();
    }
    
    @GetMapping("/role/{role}")
    public List<Shift> getShiftsByRole(@PathVariable String role) {
        return shiftRepository.findByRequiredRole(role);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shift createShift(@RequestBody @Valid CreateShiftRequest request) {
        Shift shift = Shift.builder()
            .id(UUID.randomUUID())
            .date(request.date())
            .requiredRole(request.requiredRole())
            .startTime(request.startTime())
            .endTime(request.endTime())
            .build();
        
        return shiftRepository.save(shift);
    }
    
    @PutMapping("/{shiftId}/assign/{workerId}")
    public Shift assignShift(@PathVariable UUID shiftId, @PathVariable UUID workerId) {
        Shift shift = shiftRepository.findById(shiftId)
            .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        
        shift.setWorkerId(workerId);
        return shiftRepository.save(shift);
    }
    
    @PutMapping("/{shiftId}/unassign")
    public Shift unassignShift(@PathVariable UUID shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
            .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        
        shift.setWorkerId(null);
        return shiftRepository.save(shift);
    }
}