package com.devopservice.dto;

import java.util.UUID;

public class WorkerDto {
    private UUID id;
    private String name;
    private String role;

    public WorkerDto(UUID id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}