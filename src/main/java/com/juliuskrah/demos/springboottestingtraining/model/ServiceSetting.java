package com.juliuskrah.demos.springboottestingtraining.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Data;

/**
 * @author Julius Krah
 */
@Data
@Entity
public class ServiceSetting implements Serializable {
    private static final long serialVersionUID = 3L;
    @EmbeddedId
    private ServiceSettingId id;
    private String collectionAccount;
    private boolean notifyCustomer;
    private boolean acknowledgePayer;
    private String smsSourceAddress;
    private Double floatAmount;
}
