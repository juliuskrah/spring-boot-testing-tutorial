package com.juliuskrah.demos.springboottestingtraining.model;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * @author Julius Krah
 */
@Data
@Embeddable
public class ServiceSettingId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    @ManyToOne
    private Service service;
    @ManyToOne
    private Client receiverClient;
    @ManyToOne
    private Client payerClient;
}
