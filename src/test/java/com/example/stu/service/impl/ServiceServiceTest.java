package com.example.stu.service.impl;

import com.example.stu.entity.Services;
import com.example.stu.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    @Test
    public void testGetAll() {
        List<Services> servicesList = new ArrayList<>();
        Services service1 = new Services();
        service1.setId(1L);
        service1.setName("Service 1");
        servicesList.add(service1);

        Services service2 = new Services();
        service2.setId(2L);
        service2.setName("Service 2");
        servicesList.add(service2);

        Mockito.when(serviceRepository.findAll()).thenReturn(servicesList);

        List<Services> result = serviceService.getAll();

        assertEquals(servicesList.size(), result.size());
        assertEquals(servicesList.get(0), result.get(0));
        assertEquals(servicesList.get(1), result.get(1));
    }

    @Test
    public void testGetById() {
        Services service = new Services();
        service.setId(1L);
        service.setName("Service 1");

        Mockito.when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(service));

        Services result = serviceService.getById(service.getId());

        assertEquals(service, result);
    }

    @Test
    public void testSave() {
        Services service = new Services();
        service.setId(1L);
        service.setName("Service 1");

        Mockito.when(serviceRepository.save(service)).thenReturn(service);

        Services result = serviceService.save(service);

        assertEquals(service, result);
    }

    @Test
    public void testSaveAll() {
        List<Services> servicesList = new ArrayList<>();
        Services service1 = new Services();
        service1.setId(1L);
        service1.setName("Service 1");
        servicesList.add(service1);

        Services service2 = new Services();
        service2.setId(2L);
        service2.setName("Service 2");
        servicesList.add(service2);

        Mockito.when(serviceRepository.saveAll(servicesList)).thenReturn(servicesList);

        serviceService.saveAll(servicesList);

        Mockito.verify(serviceRepository, Mockito.times(1)).saveAll(servicesList);
    }

    @Test
    public void testCount() {
        long expectedCount = 10L;
        Mockito.when(serviceRepository.count()).thenReturn(expectedCount);

        long result = serviceService.count();

        assertEquals(expectedCount, result);
    }
}