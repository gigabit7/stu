package com.example.stu.repository;

import com.example.stu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This Interface is used for interacting with database for User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * This method is used to find user by email.
     *
     * @param email email of user
     * @return User entity
     */
    User findByEmail(String email);

    /**
     * This method is used to check if user exists by email.
     *
     * @param email email of user
     * @return true if user exists, false otherwise
     */
    Boolean existsByEmail(String email);
}
