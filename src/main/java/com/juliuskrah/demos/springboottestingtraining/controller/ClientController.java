package com.juliuskrah.demos.springboottestingtraining.controller;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;
import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.service.ClientService;
import com.juliuskrah.demos.springboottestingtraining.service.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/clients")
@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CompanyService companyService;

    @GetMapping(path="/{id}")
    public ClientWithServices client(@PathVariable UUID id) {
        return clientService.getClientById(id);
    }

    @GetMapping(path="/")
    public List<ClientWithServices> clients() {
        return clientService.getAllClients();
    }

    @GetMapping(path="/{code}/code")
    public ClientWithServices clientByCode(@PathVariable String code) {
        return clientService.getClientByCode(code);
    }

    @GetMapping(path = "/{clientCode}/services")
    public List<ServiceDto> servicesByClient(@PathVariable String clientCode) {
        return companyService.findServicesForClient(clientCode);
    }
    
}
