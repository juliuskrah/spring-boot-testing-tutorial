package com.juliuskrah.demos.springboottestingtraining.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * A client object with an array of services
 * @author Julius Krah
 */
public record ClientWithServices(
    UUID id,
    String name,
    String code,
    String contactPerson,
    List<ServiceDto> services
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;
}
