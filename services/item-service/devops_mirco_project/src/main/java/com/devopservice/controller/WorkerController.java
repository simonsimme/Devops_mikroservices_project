package com.devopservice.controller;

import com.devopservice.entities.Worker;
import com.devopservice.dto.CreateWorkerRequest;
import com.devopservice.repositories.WorkerRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {
    
    private final WorkerRepository workerRepository;
    
    public WorkerController(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }
    
    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Worker getWorkerById(@PathVariable UUID id) {
        return workerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
    }
    
    @GetMapping("/role/{role}")
    public List<Worker> getWorkersByRole(@PathVariable String role) {
        return workerRepository.findByRole(role);
    }

    @GetMapping("/user/{userId}")
    public List<Worker> getWorkersByUserId(@PathVariable UUID userId) {
        return workerRepository.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Worker createWorker(@RequestBody @Valid CreateWorkerRequest request) {
        Worker worker = Worker.builder()
            .id(UUID.randomUUID())
            .name(request.name())
            .role(request.role())
            .build();
        
        return workerRepository.save(worker);
    }
}