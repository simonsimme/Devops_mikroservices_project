package com.devopservice.controller;

import com.devopservice.entities.Roles;
import com.devopservice.repositories.RolesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolesController {
    
    private final RolesRepository rolesRepository;
    
    public RolesController(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
    
    @GetMapping
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }
    
    @GetMapping("/{name}")
    public Roles getRoleByName(@PathVariable String name) {
        return rolesRepository.findById(name)
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }
}