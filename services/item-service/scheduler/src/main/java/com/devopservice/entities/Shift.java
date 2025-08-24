package com.devopservice.entities;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "Shift")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "worker_id")
    private UUID workerId;
    
    @Column(name = "required_role", nullable = false)
    private String requiredRole;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", insertable = false, updatable = false)
    private Worker worker;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "required_role", referencedColumnName = "name", insertable = false, updatable = false)
    private Roles requiredRoleEntity;
    
    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShiftAssignment> shiftAssignments;
}
