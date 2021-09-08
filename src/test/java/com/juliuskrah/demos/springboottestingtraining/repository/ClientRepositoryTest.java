package com.juliuskrah.demos.springboottestingtraining.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("Test find by Client_Code ignoring case")
    void testFindByCode() {
        var client = clientRepository.findByCodeIgnoreCase("acme");
        assertThat(client).isNotNull()
          .hasFieldOrProperty("contactPerson")
          .hasFieldOrPropertyWithValue("name", "acme corporation");
    }

    @Test
    @DisplayName("Test find by Client name ignore case")
    void testFindByClientName() {
        var client = clientRepository.findByNameIgnoreCase("hey foods");
        assertThat(client).isNotNull()
          .hasFieldOrProperty("contactPerson")
          .hasFieldOrPropertyWithValue("code", "HEY");
    }

    @Test
    @DisplayName("Test find by Client name contains")
    void testFindByClientNameContains() {
        var clients = clientRepository.findByNameContainingIgnoreCase("corp");
        assertThat(clients).isNotNull()
          .hasSize(2)
          .anySatisfy(client -> {
            assertThat(client).hasFieldOrPropertyWithValue("name", "evil corp");
            assertThat(client).hasFieldOrPropertyWithValue("contactPerson", "Tyrell Wellick");
          });
    }

    @Test
    @DisplayName("Test find by Contact person contains")
    void testFindByContactPerson() {
        var clients = clientRepository.findByContactPersonContainingIgnoreCase("We");
        assertThat(clients).isNotNull()
          .hasSize(2)
          .areAtLeast(1, new Condition<>(client -> 
            client.getName().contains("freedom"), "Client is 'freedom limited'"));
    }
}
