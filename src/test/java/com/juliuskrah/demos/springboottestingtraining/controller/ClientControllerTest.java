package com.juliuskrah.demos.springboottestingtraining.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.juliuskrah.demos.springboottestingtraining.dto.ClientWithServices;
import com.juliuskrah.demos.springboottestingtraining.dto.ServiceDto;
import com.juliuskrah.demos.springboottestingtraining.service.ClientService;
import com.juliuskrah.demos.springboottestingtraining.service.CompanyService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import lombok.RequiredArgsConstructor;

@WebMvcTest(ClientController.class)
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
class ClientControllerTest {
    private final MockMvc mvc;
    @MockBean
    private ClientService clientService;
    @MockBean
    private CompanyService companyService;

    private ClientWithServices buildClient(String name) {
        return new ClientWithServices(
            UUID.randomUUID(), 
            name, 
            name.toUpperCase(), 
            "Contact for %s".formatted(name), 
            buildServices(name)
        );
    }

    private List<ClientWithServices> buildClients(String... names) {
        return Stream.of(names)
            .map(this::buildClient)
            .toList();
    }

    private List<ServiceDto> buildServices(String... names) {
        return Stream.of(names)
            .map(name -> {
                return new ServiceDto(
                    UUID.randomUUID(), 
                    name, 
                    name.toUpperCase(), 
                    "GHS", 
                    "%s_queueName".formatted(name), 
                    "%s client".formatted(name));
            }).toList();
    }
    
    @Test
    @DisplayName("Test mapping for /clients/{id}")
    void testClient() throws Exception {
        given(clientService.findClientById(any(UUID.class))).willReturn(buildClient("EA Sports"));

        var uuid = UUID.randomUUID();
        mvc.perform(
            get("/clients/{id}", uuid).accept("application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("EA Sports")))
            .andExpect(jsonPath("$.services[0].code", is("EA SPORTS")));
    }

    @Test
    @DisplayName("Test mapping for /clients/")
    void testClients() throws Exception {
        given(clientService.findAllClients()).willReturn(buildClients("Konami", "Activision"));

        mvc.perform(get("/clients/").accept(MediaType.APPLICATION_JSON))
          .andDo(log())
          .andExpect(status().is(200))
          .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Test mapping for /clients/{code}/code")
    void testClientByCode() throws Exception {
        given(clientService.findClientByCode(anyString())).willReturn(buildClient("Crytek"));

        var mvcResult = mvc.perform(get("/clients/{code}/code", "crytek"))
            .andExpect(status().isOk())
            .andExpect(request().asyncStarted())
            .andReturn();

        this.mvc.perform(asyncDispatch(mvcResult)) 
            .andExpect(status().isOk()) 
            .andExpect(jsonPath("$.name", is("Crytek")))
            .andExpect(jsonPath("$.code", is("CRYTEK")));
    }

    @Test
    @DisplayName("Test mapping for /clients/{clientCode}/services")
    void testServicesByClient() throws Exception {
        given(companyService.findAllServices()).willReturn(buildServices("FIFA 2021", "Need for Speed"));
        
        mvc.perform(get("/clients/{clientCode}/services", "Rocksteady Studios"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().encoding("ISO-8859-1"));

        verify(companyService, atMost(1)).findAllServices();
    }
}
