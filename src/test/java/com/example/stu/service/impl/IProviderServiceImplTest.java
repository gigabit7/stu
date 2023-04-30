package com.example.stu.service.impl;

import com.example.stu.entity.ServiceProvider;
import com.example.stu.repository.ServiceProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IProviderServiceImplTest {

    @Mock
    private ServiceProviderRepository serviceProviderRepository;

    @InjectMocks
    private IProviderServiceImpl providerService;

    @Test
    public void testFindByUserId() {
        ServiceProvider expectedProvider = new ServiceProvider();
        Long userId = 1L;
        when(serviceProviderRepository.findByUserId(userId)).thenReturn(Optional.of(expectedProvider));
        ServiceProvider actualProvider = providerService.findByUserId(userId);
        verify(serviceProviderRepository, times(1)).findByUserId(userId);
        assertEquals(expectedProvider, actualProvider);
    }

    @Test
    public void testFindById() {
        ServiceProvider expectedProvider = new ServiceProvider();
        Long id = 1L;
        when(serviceProviderRepository.findById(id)).thenReturn(Optional.of(expectedProvider));
        ServiceProvider actualProvider = providerService.findById(id);
        verify(serviceProviderRepository, times(1)).findById(id);
        assertEquals(expectedProvider, actualProvider);
    }

    @Test
    public void testFindByUserEmail() {
        ServiceProvider expectedProvider = new ServiceProvider();
        String email = "test@example.com";
        when(serviceProviderRepository.findByUserEmail(email)).thenReturn(Optional.of(expectedProvider));
        ServiceProvider actualProvider = providerService.findByUserEmail(email);
        verify(serviceProviderRepository, times(1)).findByUserEmail(email);
        assertEquals(expectedProvider, actualProvider);
    }

    @Test
    public void testSave() {
        ServiceProvider serviceProvider = new ServiceProvider();
        providerService.save(serviceProvider);
        verify(serviceProviderRepository, times(1)).save(serviceProvider);
    }




}