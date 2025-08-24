package com.devopservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ShiftAssignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftAssignment {
    
    @Id
    private UUID id;
    
    @Column(name = "shift_id", nullable = false)
    private UUID shiftId;
    
    @Column(name = "worker_id", nullable = false)
    private UUID workerId;
    
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", insertable = false, updatable = false)
    private Shift shift;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", insertable = false, updatable = false)
    private Worker worker;
    
    @PrePersist
    protected void onCreate() {
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }
}