package com.juliuskrah.demos.springboottestingtraining.model;

import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * @author Julius Krah
 */
@Data
@Entity
public class Service implements Serializable {
    private static final long serialVersionUID = 2L;
    @Id
    private UUID id;
    private String name;
    private String code;
    private String currency;
    private String queueName;
    @ManyToOne(fetch = LAZY)
    private Client client;
}
