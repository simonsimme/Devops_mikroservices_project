package com.devopservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devopservice.entities.Shift;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findByDate(LocalDate date);
    List<Shift> findByWorkerIdIsNull();
    List<Shift> findByRequiredRole(String requiredRole);
    
    @Query("SELECT s FROM Shift s WHERE s.date BETWEEN ?1 AND ?2")
    List<Shift> findShiftsBetweenDates(LocalDate startDate, LocalDate endDate);
}