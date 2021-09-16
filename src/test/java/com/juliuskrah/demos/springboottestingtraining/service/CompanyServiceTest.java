package com.juliuskrah.demos.springboottestingtraining.service;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.model.Client;
import com.juliuskrah.demos.springboottestingtraining.model.Service;
import com.juliuskrah.demos.springboottestingtraining.repository.ServiceRepository;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Julius Krah
 */
@SpringBootTest(classes = { CompanyServiceImpl.class })
class CompanyServiceTest {
    @MockBean
    private ServiceRepository serviceRepository;

    private Service buildService(String name) {
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
    }

    private List<Service> buiServices(String... names) {
        return Stream.of(names)
            .map(this::buildService)
            .toList();
    }

    @Test
    @DisplayName("Test find service by id")
    void testFindById(@Autowired CompanyService companyService) {
        given(serviceRepository.findById(any(UUID.class))).willReturn(Optional.of(buildService("jeremy's bakery")));

        var name = new Condition<ServiceDto>(service -> 
            service.name().equals("jeremy's bakery"), "Service has name: build and construct");
        var service = companyService.findServiceById(UUID.randomUUID());
        then(service).is(name);
    }

    @Test
    @DisplayName("Test find service by code")
    void testFindByCode(@Autowired CompanyService companyService) {
        given(serviceRepository.findById(any(UUID.class))).willReturn(Optional.of(buildService("build and construct")));

        var code = new Condition<ServiceDto>(service -> 
            service.code().equals("BUILD AND CONSTRUCT"), "Service has code: BUILD AND CONSTRUCT");
        var name = new Condition<ServiceDto>(service -> 
            service.name().equals("build and construct"), "Service has name: build and construct");
        var service = companyService.findServiceById(UUID.randomUUID());
        then(service).has(allOf(code, name));
    }

    
    @Test
    @DisplayName("Test find all services")
    void testFindAll(@Autowired CompanyService companyService) {
        given(serviceRepository.findAll()).willReturn(buiServices("rick & morty", "family guy"));

        var services = companyService.findAllServices();
        var series = List.of("rick & morty", "family guy");
        var tvShows = new Condition<String>(series::contains, "TV shows");
        then(services).extracting("name", String.class).are(tvShows);
    }

    @Test
    @DisplayName("Test find all services by client code")
    void testFindAllByClientCode(@Autowired CompanyService companyService) {
        given(serviceRepository.findByClientCodeIgnoreCase(anyString())).willReturn(buiServices("wanda vision"));

        var services = companyService.findServicesForClient("wanda");
        assumeThat(services).isNotEmpty();
        then(services).hasSize(1)
            .usingElementComparatorIgnoringFields("currency", "name", "id", "client")
            .containsExactlyInAnyOrder(new ServiceDto(null, null, "WANDA VISION", null, "wanda vision_queueName", ""));
    }
}
