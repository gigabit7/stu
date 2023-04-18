package com.example.stu.runner;

import com.example.stu.entity.Role;
import com.example.stu.entity.RoleName;
import com.example.stu.entity.Services;
import com.example.stu.entity.User;
import com.example.stu.repository.RoleRepository;
import com.example.stu.service.IServiceService;
import com.example.stu.service.IUserService;
import com.example.stu.web.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to initialize the database with some default values.
 * It is executed when the application starts.
 */
@Component
@AllArgsConstructor
public class DatabaseInitialization implements ApplicationRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addRolesIntoDb();
    }

    /**
     * Creates a role if it does not exist in the database.
     *
     * @param name the name of the role
     */
    private void createRoleIfNotFound(final RoleName name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
    }

    /**
     * Adds roles into the database.
     */
    private void addRolesIntoDb() {
        createRoleIfNotFound(RoleName.ROLE_ADMIN);
        createRoleIfNotFound(RoleName.ROLE_USER);
        createRoleIfNotFound(RoleName.ROLE_PROVIDER);
    }
}
