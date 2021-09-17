package com.example.repository;

import com.example.customrepositories.ClearanceRepositoryCustom;
import com.example.entity.DOClearance;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClearanceRepository extends JpaRepository<DOClearance, String>,ClearanceRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update clearance cl set cl.deleted = ?1 where cl.id = ?2", nativeQuery = true)
    int deleteClearance(boolean deleted, String id);
    
    @Query(value = "update clearance cl set cl.status = ?1 where cl.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(cl.id) > 0 then 'true' else 'false' end from clearance cl where cl.id = ?1", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from clearance cl where cl.actual_date > = ?1 and cl.actual_date < = ?2 and cl.status = 'NEW'", nativeQuery = true)
    ArrayList<DOClearance> getNotificationList(long start, long end);
}
