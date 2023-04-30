package com.example.stu.repository;

import com.example.stu.entity.Role;
import com.example.stu.entity.RoleName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class RoleRepositoryTest {
    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindByName() {
        RoleName roleName = RoleName.ROLE_USER;
        Role role = new Role(roleName);

        Mockito.when(roleRepository.findByName(roleName)).thenReturn(role);

        Role result = roleRepository.findByName(roleName);

        assertEquals(role, result);
    }
}