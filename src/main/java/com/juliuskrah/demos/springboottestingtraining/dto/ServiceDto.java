package com.juliuskrah.demos.springboottestingtraining.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * A flat service object with no client name
 * @author Julius Krah
 */
public record ServiceDto(
    UUID id,
    String name,
    String code,
    String currency,
    String queueName,
    String client
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;
}
