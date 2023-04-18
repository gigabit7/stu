package com.example.stu.service.impl;

import com.example.stu.entity.Services;
import com.example.stu.repository.ServiceRepository;
import com.example.stu.service.IServiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * This class implements the methods defined in the IServiceService interface.
 * See the interface for more details.
 */
@Service
@AllArgsConstructor
public class ServiceService implements IServiceService {
    private final ServiceRepository serviceRepository;

    public List<Services> getAll() {
        return serviceRepository.findAll();
    }

    public Services getById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @Override
    public Services save(Services service) {
        return serviceRepository.save(service);
    }

    @Override
    public void saveAll(List<Services> servicesArrayList) {
        serviceRepository.saveAll(servicesArrayList);
    }

    @Override
    public Long count() {
        return serviceRepository.count();
    }
}
