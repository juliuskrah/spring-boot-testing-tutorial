package com.juliuskrah.demos.springboottestingtraining.repository;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.model.Client;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Julius Krah
 */
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByCodeIgnoreCase(String code);
    Client findByNameIgnoreCase(String name);
    List<Client> findByNameContainingIgnoreCase(String name);
    List<Client> findByContactPersonContainingIgnoreCase(String contactPerson);
}
