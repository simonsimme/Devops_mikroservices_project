package com.devopservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devopservice.entities.ShiftAssignment;

import java.util.UUID;
import java.util.List;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, UUID> {
    List<ShiftAssignment> findByWorkerId(UUID workerId);
    List<ShiftAssignment> findByShiftId(UUID shiftId);
}
