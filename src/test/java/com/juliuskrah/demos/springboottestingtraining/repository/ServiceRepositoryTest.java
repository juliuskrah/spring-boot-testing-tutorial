package com.juliuskrah.demos.springboottestingtraining.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

import com.juliuskrah.demos.springboottestingtraining.model.Client;
import com.juliuskrah.demos.springboottestingtraining.model.Service;

import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import lombok.RequiredArgsConstructor;

/**
 * @author Julius Krah
 */
@DataJpaTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = ALL)
class ServiceRepositoryTest {
    private final ServiceRepository serviceRepository;

    @Test
    @DisplayName("Test find by service name ignoring case")
    void findByName() {
        var service = serviceRepository.findByNameIgnoreCase("acme bomb");
        assertThat(service).isNotNull()
            .extracting(Service::getCode, Service::getCurrency, Service::getQueueName)
            .containsExactly("ACME_BOMB", "GHS", "queue_acme_bomb");
    }

    @Test
    @DisplayName("Test find by service code ignoring case")
    void findByCode() {
        var service = serviceRepository.findByCodeIgnoreCase("blackmailing");
        assertThat(service).isNotNull()
            .hasFieldOrPropertyWithValue("name", "blackmailing")
            .hasFieldOrPropertyWithValue("currency", "USD")
            .hasFieldOrPropertyWithValue("queueName", "queue_blackmailing")
            .extracting("client", InstanceOfAssertFactories.type(Client.class))
            .hasFieldOrPropertyWithValue("name", "evil corp")
            .hasFieldOrPropertyWithValue("contactPerson", "Tyrell Wellick");
    }

    @Test
    @DisplayName("Test find by service queue name ignoring case")
    void findByQueueName() {
        var services = serviceRepository.findByQueueNameContainingIgnoreCase("queue");
        AssertFactory<Service, ObjectAssert<Service>> objectAssertFactory =
            (service) -> new ObjectAssert<>(service);
        assertThat(services, objectAssertFactory).hasSize(8)
            .last().hasFieldOrPropertyWithValue("name", "free health");
    }

    @Test
    @DisplayName("Test find by client name ignoring case")
    void findByClientName() {
        var services = serviceRepository.findByClientNameIgnoreCase("freedom limited");
        assertThat(services).isNotEmpty()
            .filteredOn(service -> "FREE_HEALTH".equals(service.getCode()))
            .allSatisfy(service -> {
                assertThat(service).extracting(Service::getName).isEqualTo("free health");
                assertThat(service).extracting(Service::getQueueName).isEqualTo("queue_free_health");
            });
    }

    @Test
    @DisplayName("Test find by client code ignoring case")
    void findByClientCode() {
        var services = serviceRepository.findByClientCodeIgnoreCase("hey");
        assertThat(services).isNotEmpty()
            .element(1)
            .hasFieldOrPropertyWithValue("name", "coffee")
            .hasFieldOrPropertyWithValue("queueName", "queue_coffee");
    }
}
