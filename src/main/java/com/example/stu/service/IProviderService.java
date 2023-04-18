package com.example.stu.service;

import com.example.stu.entity.ServiceProvider;
import org.springframework.security.core.Authentication;

/**
 * This interface defines the methods that will be used to manage service providers.
 */
public interface IProviderService {
    /**
     * This method will return a service provider from the database by User Id.
     *
     * @param id The id of the user to be returned.
     * @return The service provider from the database.
     */
    ServiceProvider findByUserId(Long id);

    /**
     * This method will return a service provider from the database by I'd.
     *
     * @param id The id of the service provider to be returned.
     * @return The service provider from the database.
     */
    ServiceProvider findById(Long id);

    /**
     * This method will return a service provider from the database by email.
     *
     * @param email The email of the service provider to be returned.
     * @return The service provider from the database.
     */
    ServiceProvider findByUserEmail(String email);

    /**
     * This method will save a service provider to the database.
     *
     * @param serviceProvider The service provider to be saved.
     */
    void save(ServiceProvider serviceProvider);

    /**
     * This method will return a boolean value if the user is a service provider.
     *
     * @param authentication The authentication to be checked.
     * @return A boolean value if the user is a service provider.
     */
    Boolean checkIfProviderFromAuthentication(Authentication authentication);
}
