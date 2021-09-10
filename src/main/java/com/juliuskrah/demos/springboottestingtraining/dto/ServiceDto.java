package com.juliuskrah.demos.springboottestingtraining.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Value;

/**
 * A flat service object with no client name
 * @author Julius Krah
 */
@Value
public class ServiceDto implements Serializable {
    private static final long serialVersionUID = 6L;
    
    private UUID id;
    private String name;
    private String code;
    private String currency;
    private String queueName;
    private String client;
}
