package com.example.stu.repository;

import com.example.stu.entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface is used to interact with the database for Service Providers.
 */
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    /**
     * This method is used to find a service provider by user id.
     *
     * @param id the id of the user.
     * @return a service provider.
     */
    Optional<ServiceProvider> findByUserId(Long id);

    /**
     * This method is used to find a service provider by user email.
     *
     * @param email the email of the user.
     * @return a service provider.
     */
    Optional<ServiceProvider> findByUserEmail(String email);
}
