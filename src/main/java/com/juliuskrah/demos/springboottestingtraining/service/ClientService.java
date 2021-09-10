package com.juliuskrah.demos.springboottestingtraining.service;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Julius Krah
 */
public interface ClientService {
    /**
     * Find a client given an id
     * @param id the identity of a client
     * @return the client with the given identity or null if non is found
     */
    @Nullable ClientWithServices getClientById(@NonNull UUID id);

    /**
     * Find all clients
     * @return all clients
     */
    List<ClientWithServices> getAllClients();
}
