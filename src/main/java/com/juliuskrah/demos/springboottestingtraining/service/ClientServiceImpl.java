package com.juliuskrah.demos.springboottestingtraining.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;
import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.model.Client;
import com.juliuskrah.demos.springboottestingtraining.repository.ClientRepository;
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
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
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

    private List<ServiceDto> toServiceDto(List<com.juliuskrah.demos.springboottestingtraining.model.Service> services) {
        return services.stream()
            .map(this::toServiceDto)
            .toList();
    }

    private ClientWithServices toClientDto(Client client) {
        var services = serviceRepository.findByClientCodeIgnoreCase(client.getCode());
        var cws = new ClientWithServices(
            client.getId(), 
            client.getName(),
            client.getCode(), 
            client.getContactPerson(), 
            toServiceDto(services));
        return cws;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientWithServices getClientById(UUID id) {
        return clientRepository.findById(id)
            .map(this::toClientDto).orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientWithServices getClientByCode(String code) {
        return Optional.ofNullable(
            clientRepository.findByCodeIgnoreCase(code)
        ).map(this::toClientDto).orElseThrow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClientWithServices> getAllClients() {
        return clientRepository.findAll()
            .stream().map(this::toClientDto)
            .toList();
    }

}
