package com.juliuskrah.demos.springboottestingtraining.repository;

import java.util.List;
import java.util.UUID;

import com.juliuskrah.demos.springboottestingtraining.model.ServiceSetting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Julius Krah
 */
public interface ServiceSettingRepository extends JpaRepository<ServiceSetting, UUID> {
    List<ServiceSetting> findByCollectionAccountContainingIgnoreCase(String collectionAccount);

    @Query("""
            SELECT 
                ss 
            FROM 
                ServiceSetting ss 
            where 
                ss.id.payerClient.id = :id
            """)
    List<ServiceSetting> findByPayerClientId(UUID id);

    @Query("""
            SELECT 
                ss 
            FROM 
                ServiceSetting ss 
            WHERE 
                ss.id.receiverClient.id = :id
            """)
    List<ServiceSetting> findByReceiverClientId(UUID id);

    @Query("""
            SELECT
                ss
            FROM
                ServiceSetting ss
            WHERE
                ss.id.service.id = :id
            """)
    List<ServiceSetting> findByServiceId(UUID id);
}
