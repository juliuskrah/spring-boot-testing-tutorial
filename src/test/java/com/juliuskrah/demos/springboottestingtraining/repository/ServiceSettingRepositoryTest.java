package com.juliuskrah.demos.springboottestingtraining.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.notIn;
import static org.assertj.core.api.Assertions.tuple;

import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.model.Client;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * @author Julius Krah
 */
@DataJpaTest
class ServiceSettingRepositoryTest {
    @Autowired
    private ServiceSettingRepository serviceSettingRepository;

    @Test
    @DisplayName("Test find by collection account containing ignorecase")
    void testFindByCollectionAccount() {
        var serviceSettings = serviceSettingRepository.findByCollectionAccountContainingIgnoreCase("secret offshore");
        assertThat(serviceSettings).isNotEmpty()
            .hasSize(2)
            .doesNotHaveDuplicates()
            .filteredOn("id.receiverClient.name", notIn("evil corp"))
            .isEmpty();
    }

    @Test
    @DisplayName("Test find by payer client Id")
        void testFindByPayerClientId() {
            var serviceSettings = serviceSettingRepository.findByPayerClientId(UUID.fromString("15759e2d-21ab-4ec2-bb9a-2aee55c409fe"));
            assertThat(serviceSettings).isNotEmpty()
                .hasSize(1)
                .extracting("acknowledgePayer").contains(true);
        }

    @Test
    @DisplayName("Test find by receiver client Id")
    void testFindByReceiverClientId() {
        var serviceSettings = serviceSettingRepository.findByReceiverClientId(UUID.fromString("b0c02363-5031-4c0c-9a83-f242df4039b1"));
        assertThat(serviceSettings).isNotEmpty()
            .hasSize(3)
            .extracting("notifyCustomer", "acknowledgePayer")
            .contains(
                tuple(false, true), 
                tuple(false, false), 
                tuple(true, true)
            );
    }

    @Test
    @DisplayName("Test find by service id")
    void testFindByServiceId() {
        var serviceSettings = serviceSettingRepository.findByServiceId(UUID.fromString("69fe1c30-57c0-44b4-a1cb-b398890174f3"));
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(serviceSettings).as("Service setting must not be null").isNotNull();
            softly.assertThat(serviceSettings).first()
                .extracting("id.payerClient",
                    InstanceOfAssertFactories.type(Client.class)
            ).hasFieldOrPropertyWithValue("name", "acme corporation");
            softly.assertThat(serviceSettings).last()
                .extracting("id.receiverClient",
                InstanceOfAssertFactories.type(Client.class)
            ).hasFieldOrPropertyWithValue("name", "hey foods");
        });
    }
}
