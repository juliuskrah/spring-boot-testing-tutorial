package com.juliuskrah.demos.springboottestingtraining.repository;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.model.Service;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Julius Krah
 */
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    Service findByNameIgnoreCase(String name);
    Service findByCodeIgnoreCase(String code);
    List<Service> findByQueueNameContainingIgnoreCase(String queueName);
    List<Service> findByClientNameIgnoreCase(String name);
    List<Service> findByClientCodeIgnoreCase(String code);
}
