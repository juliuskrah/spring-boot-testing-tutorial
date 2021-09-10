package com.juliuskrah.demos.springboottestingtraining.dto;

import java.util.UUID;
import java.io.Serializable;
import java.util.List;
import lombok.Value;

/**
 * A client object with an array of services
 * @author Julius Krah
 */
@Value
public class ClientWithServices implements Serializable {
    private static final long serialVersionUID = 5L;

    private UUID id;
    private String name;
    private String code;
    private String contactPerson;
    private List<ServiceDto> services;
}
