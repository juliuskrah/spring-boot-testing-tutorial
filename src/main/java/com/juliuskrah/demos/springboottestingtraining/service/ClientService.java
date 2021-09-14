package com.juliuskrah.demos.springboottestingtraining.service;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;

import org.springframework.lang.NonNull;

/**
 * @author Julius Krah
 */
public interface ClientService {
    /**
     * Find a client given an id
     * @param id the identity of a client
     * @return the client with the given identity or null if non is found
     */
    ClientWithServices getClientById(@NonNull UUID id);

    /**
     * Find a client given a code
     * @param code the client code
     * @return the client with the given code of null if non is found
     */
    ClientWithServices getClientByCode(String code);

    /**
     * Find all clients
     * @return all clients
     */
    List<ClientWithServices> getAllClients();
}
