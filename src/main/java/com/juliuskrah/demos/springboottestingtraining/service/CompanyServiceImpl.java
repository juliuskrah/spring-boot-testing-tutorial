package com.juliuskrah.demos.springboottestingtraining.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.repository.ServiceRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * @author Julius Krah
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final ServiceRepository serviceRepository;

    private ServiceDto toServiceDto(@NonNull com.juliuskrah.demos.springboottestingtraining.model.Service service) {
        return new ServiceDto(
            service.getId(), 
            service.getName(), 
            service.getCode(), 
            service.getCurrency(), 
            service.getQueueName(), 
            service.getClient().getName()
        );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceDto findServiceById(UUID id) {
        return serviceRepository.findById(id)
            .map(this::toServiceDto)
            .orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceDto findServiceByCode(String code) {
        return Optional.ofNullable(
            serviceRepository.findByCodeIgnoreCase(code)
        ).map(this::toServiceDto)
        .orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceDto> findAllServices() {
        return serviceRepository.findAll()
            .stream()
            .map(this::toServiceDto)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceDto> findServicesForClient(String clientCode) {
        return serviceRepository.findByClientCodeIgnoreCase(clientCode)
            .stream()
            .map(this::toServiceDto)
            .toList();
    }
    
}
