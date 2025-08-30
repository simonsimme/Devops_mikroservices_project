package com.devopservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devopservice.entities.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, String> {
}