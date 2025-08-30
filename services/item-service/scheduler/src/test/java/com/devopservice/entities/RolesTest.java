package com.devopservice.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RolesTest {
    @Test
    void testRoleTypeEnumValues() {
        assertEquals("floor", Roles.RoleType.FLOOR.getValue());
        assertEquals("floor-manager", Roles.RoleType.FLOOR_MANAGER.getValue());
        assertEquals("administration", Roles.RoleType.ADMINISTRATION.getValue());
        assertEquals("manager", Roles.RoleType.MANAGER.getValue());
    }

    @Test
    void testRolesBuilderAndGetters() {
        Roles roles = Roles.builder().name("floor").build();
        assertEquals("floor", roles.getName());
    }
}
