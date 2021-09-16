package com.juliuskrah.demos.springboottestingtraining.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Julius Krah
 */
@Data
@Entity
public class Client implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private UUID id;
    private String name;
    private String code;
    private String contactPerson;
}
