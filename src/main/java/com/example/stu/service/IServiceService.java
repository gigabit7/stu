package com.example.stu.service;

import com.example.stu.entity.Booking;
import com.example.stu.entity.Services;

import java.util.List;

/**
 * This interface defines the methods that will be used to manage services.
 */
public interface IServiceService {
    /**
     * This method will return List of services from the database.
     *
     * @return List of services from the database.
     */
    List<Services> getAll();

    /**
     * This method will return a service from the database.
     *
     * @param id The id of the service to be returned.
     * @return The service with the given id.
     */
    Services getById(Long id);

    /**
     * This method will save a service into the database.
     *
     * @param service The name of the service to be returned.
     * @return The service with that saved.
     */
    Services save(Services service);
/**
     * This method will save a list of services into the database.
     *
     * @param servicesArrayList The list of the services to be saved.
     */
    void saveAll(List<Services> servicesArrayList);
    Long count();
}
