package com.devopservice.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roles {
    @Id
    @Column(name = "name")
    private String name;

    public enum RoleType {
        FLOOR("floor"),
        FLOOR_MANAGER("floor-manager"),
        ADMINISTRATION("administration"),
        MANAGER("manager");
        
        private final String value;
        
        RoleType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
