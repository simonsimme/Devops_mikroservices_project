package com.devopservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devopservice.entities.Worker;

import java.util.UUID;
import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID> {
    List<Worker> findByRole(String role);
    
    @Query("SELECT w FROM Worker w WHERE w.role = ?1")
    List<Worker> findWorkersByRole(String role);
}
