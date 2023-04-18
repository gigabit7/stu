package com.example.stu.service.impl;

import com.example.stu.entity.ServiceProvider;
import com.example.stu.repository.ServiceProviderRepository;
import com.example.stu.service.IProviderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
/** This class implements the methods defined in the IProviderService interface.
 * See the IProviderService interface for more information.
 * */
@Service
@AllArgsConstructor
public class IProviderServiceImpl implements IProviderService {
    private final ServiceProviderRepository serviceProviderRepository;
    @Override
    public ServiceProvider findByUserId(Long id) {
        return serviceProviderRepository.findByUserId(id).orElse(null);
    }

    @Override
    public ServiceProvider findById(Long id) {
        return serviceProviderRepository.findById(id).orElse(null);
    }

    @Override
    public ServiceProvider findByUserEmail(String email) {
        return serviceProviderRepository.findByUserEmail(email).orElse(null);
    }

    @Override
    public void save(ServiceProvider serviceProvider) {
        serviceProviderRepository.save(serviceProvider);
    }

    @Override
    public Boolean checkIfProviderFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_PROVIDER"));
    }
}
