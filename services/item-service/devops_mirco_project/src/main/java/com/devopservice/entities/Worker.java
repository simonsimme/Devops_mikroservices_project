package com.devopservice.entities;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


@Entity
@Table(name = "worker")
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
@JsonIgnore  // <-- Add this annotation
private Roles roleEntity;
    
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  
    private List<Shift> shifts;
    
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  
    private List<ShiftAssignment> shiftAssignments;
}
