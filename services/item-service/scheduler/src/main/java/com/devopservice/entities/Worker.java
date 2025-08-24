package com.devopservice.entities;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.List;


@Entity
@Table(name = "Worker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String role;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role", referencedColumnName = "name", insertable = false, updatable = false)
    private Roles roleEntity;
    
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shift> shifts;
    
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShiftAssignment> shiftAssignments;
}
