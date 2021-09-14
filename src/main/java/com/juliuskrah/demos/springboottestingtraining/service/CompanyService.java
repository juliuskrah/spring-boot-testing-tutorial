package com.juliuskrah.demos.springboottestingtraining.service;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;

/**
 * @author Julius Krah
 */
public interface CompanyService {
    /**
     * Find a service given an id
     * @param id
     * @return a service identified by the id
     */
    ServiceDto findServiceById(UUID id);

    /**
     * Find a service given a service code
     * @param code
     * @return a service with the given code
     */
    ServiceDto findServiceByCode(String code);

    /**
     * Find all services
     * @return all services
     */
    List<ServiceDto> findAllServices();

    /**
     * Find all services for client with given code
     * @param clientCode the client id
     * @return all services for the provided client code
     */
    List<ServiceDto> findServicesForClient(String clientCode);
}
