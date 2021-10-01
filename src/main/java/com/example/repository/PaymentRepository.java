package com.example.repository;

import com.example.customrepositories.PaymentRepositoryCustom;
import com.example.entity.DOPayment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<DOPayment, String>,PaymentRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update payment p set p.deleted = ?1 where p.id = ?2", nativeQuery = true)
    int deletePayment(boolean deleted, String id);

    @Query(value = "select case when count(p.id) > 0 then 'true' else 'false' end from payment p where p.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from payment where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOPayment> getItemsByProjectId(String projectId);

}
