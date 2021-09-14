package com.juliuskrah.demos.springboottestingtraining.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;
import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.model.Client;
import com.juliuskrah.demos.springboottestingtraining.model.Service;
import com.juliuskrah.demos.springboottestingtraining.repository.ClientRepository;
import com.juliuskrah.demos.springboottestingtraining.repository.ServiceRepository;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

    private List<Client> buildClients(String... names) {
        return Stream.of(names)
            .map(this::buildClient)
            .toList();
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

    @Test
    @DisplayName("Test find client by code")
    void testFindByCode() {
        when(serviceRepository.findByClientCodeIgnoreCase(anyString())).thenReturn(
            buildServices("freedom clothing", "freedom of speech", "freedom foods")
        );
        when(clientRepository.findByCodeIgnoreCase(anyString())).thenReturn(buildClient("freedom"));

        var client = clientService.getClientByCode("FREEDOM");
        assertThat(client).isNotNull()
            .extracting("services", InstanceOfAssertFactories.LIST)
            .hasOnlyElementsOfType(ServiceDto.class)
            .anySatisfy(service -> {
                assertThat(service).hasFieldOrPropertyWithValue("client", "freedom of speech client");
            });

        verify(serviceRepository).findByClientCodeIgnoreCase(anyString());
        verify(clientRepository, only()).findByCodeIgnoreCase(anyString());
    }

    @Test
    @DisplayName("Test find all clients")
    void testFindAll() {
        when(serviceRepository.findByClientCodeIgnoreCase(anyString())).then(
            answer((String code) -> {
                return switch (code) {
                    case "BECL'S PIZZA"        -> buildServices("broccoli toppings", "sausage toppings");
                    case "CHARGE CARPENTARY"   -> buildServices("wardrobe", "office chair", "sofas");
                    default                    -> throw new IllegalArgumentException("Unexpected value: " + code);
                };
            })
        );
        when(clientRepository.findAll()).thenReturn(buildClients("becl's pizza", "charge carpentary"));

        var clients = clientService.getAllClients();
        var expected = List.of(
            new ServiceDto(null, "broccoli toppings", "BROCCOLI TOPPINGS", null, "broccoli toppings_queueName", "broccoli toppings client"),
            new ServiceDto(null, "sausage toppings", "SAUSAGE TOPPINGS", null, "sausage toppings_queueName", "sausage toppings client"),
            new ServiceDto(null, "wardrobe", "WARDROBE", null, "wardrobe_queueName", "wardrobe client"),
            new ServiceDto(null, "office chair", "OFFICE CHAIR", null, "office chair_queueName", "office chair client"),
            new ServiceDto(null, "sofas", "SOFAS", null, "sofas_queueName", "sofas client")
        );
        assertThat(clients).isNotEmpty()
            .hasSize(2)
            .flatExtracting("services")
            .usingRecursiveComparison()
            .ignoringFields("id", "currency")
            .isEqualTo(expected);

        ArgumentCaptor<String> captureClientCode = ArgumentCaptor.forClass(String.class);
        verify(serviceRepository, atLeast(1)).findByClientCodeIgnoreCase("BECL'S PIZZA");
        verify(serviceRepository, atLeast(1)).findByClientCodeIgnoreCase(captureClientCode.capture());
        verify(clientRepository, only()).findAll();

        assertThat(captureClientCode.getAllValues())
            .contains("BECL'S PIZZA", Index.atIndex(0))
            .contains("CHARGE CARPENTARY", Index.atIndex(1));
    }

    @Test
    @DisplayName("Test find client by id throws exception")
    void testFindByIdThrows() {
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() 
            -> clientService.getClientById(UUID.fromString("7602bbdd-ec04-4337-993e-3fdb1be76310"))
        ).withMessage("No value present")
        .withNoCause();
    }

    @Test
    @DisplayName("Test find client by code throws exception")
    void testFindByCodeThrows() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() 
            -> clientService.getClientByCode(anyString())
        ).withMessage("No value present")
        .withNoCause();
    }
}
