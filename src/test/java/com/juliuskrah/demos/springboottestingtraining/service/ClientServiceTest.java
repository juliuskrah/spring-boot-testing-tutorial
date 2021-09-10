package com.juliuskrah.demos.springboottestingtraining.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;
import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.model.Client;
import com.juliuskrah.demos.springboottestingtraining.model.Service;
import com.juliuskrah.demos.springboottestingtraining.repository.ClientRepository;
import com.juliuskrah.demos.springboottestingtraining.repository.ServiceRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Julius Krah
 */
@SpringBootTest(classes = { ClientServiceImpl.class })
class ClientServiceTest {
    @Autowired
    private ClientService clientService;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private ServiceRepository serviceRepository;

    private List<Service> buildServices(String... names) {
        return Stream.of(names)
            .map(name -> {
                var service = new Service();
                service.setId(UUID.randomUUID());
                service.setName(name);
                service.setCode(name.toUpperCase());
                service.setCurrency("GHS");
                service.setQueueName("%s_queueName".formatted(name));
                var client = new Client();
                client.setName("%s client".formatted(name));
                service.setClient(client);
                return service;
            }).toList();
    }

    private Client buildClient(String name) {
        var client = new Client();
        client.setCode(name.toUpperCase());
        client.setContactPerson("%s's contact".formatted(name));
        client.setName(name);
        client.setId(UUID.randomUUID());
        return client;
    }

    @Test
    @DisplayName("Test find client by id")
    void testFindById() {
        when(serviceRepository.findByClientCodeIgnoreCase(anyString())).thenReturn(
            buildServices("acme blackmail", "acme hacking")
        );
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(buildClient("acme")));

        var expected = new ClientWithServices(null, "acme", "ACME", "acme's contact", 
            List.of(
                new ServiceDto(null, "acme blackmail", "ACME BLACKMAIL", "GHS", "acme blackmail_queueName", "acme blackmail client"),
                new ServiceDto(null, "acme hacking", "ACME HACKING", "GHS", "acme hacking_queueName", "acme hacking client")
            )
        );

        var client = clientService.getClientById(UUID.fromString("7602bbdd-ec04-4337-993e-3fdb1be76310"));
        assertThat(client).isNotNull()
            .usingRecursiveComparison()
            .ignoringExpectedNullFields().isEqualTo(expected);

        verify(serviceRepository, times(1)).findByClientCodeIgnoreCase(anyString());
    }


}
