package com.example.stu.repository;

import com.example.stu.entity.ServiceProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceProviderRepositoryTest {

    @Mock
    private ServiceProviderRepository serviceProviderRepository;

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        ServiceProvider serviceProvider = new ServiceProvider();

        when(serviceProviderRepository.findByUserId(userId)).thenReturn(Optional.of(serviceProvider));

        Optional<ServiceProvider> result = serviceProviderRepository.findByUserId(userId);

        verify(serviceProviderRepository).findByUserId(userId);
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(serviceProvider);
    }

    @Test
    public void testFindByUserEmail() {
        String email = "test@example.com";
        ServiceProvider serviceProvider = new ServiceProvider();

        when(serviceProviderRepository.findByUserEmail(email)).thenReturn(Optional.of(serviceProvider));

        Optional<ServiceProvider> result = serviceProviderRepository.findByUserEmail(email);

        verify(serviceProviderRepository).findByUserEmail(email);
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(serviceProvider);
    }
}
