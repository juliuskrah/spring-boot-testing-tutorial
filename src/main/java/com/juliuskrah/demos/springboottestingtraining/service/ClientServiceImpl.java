package com.juliuskrah.demos.springboottestingtraining.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;
import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.model.Client;
import com.juliuskrah.demos.springboottestingtraining.repository.ClientRepository;
import com.juliuskrah.demos.springboottestingtraining.repository.ServiceRepository;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Julius Krah
 */
@Service
@Transactional
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
    public List<ClientWithServices> getAllClients() {
        // TODO Auto-generated method stub
        return null;
    }

}
