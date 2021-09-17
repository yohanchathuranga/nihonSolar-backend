package com.example.repository;

import com.example.customrepositories.InsuranceRepositoryCustom;
import com.example.entity.DOInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InsuranceRepository extends JpaRepository<DOInsurance, String>,InsuranceRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update insurance i set i.deleted = ?1 where i.id = ?2", nativeQuery = true)
    int deleteInsurance(boolean deleted, String id);
    
    @Query(value = "update insurance i set i.status = ?1 where i.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(i.id) > 0 then 'true' else 'false' end from insurance i where i.id = ?1", nativeQuery = true)
    boolean isExistsById(String id);


}
