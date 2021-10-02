package com.nihon.repository;

import com.nihon.customrepositories.ClearanceRepositoryCustom;
import com.nihon.entity.DOClearance;
import java.util.ArrayList;
import java.util.List;
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
    
    @Transactional
    @Modifying
    @Query(value = "update clearance cl set cl.status = ?1 where cl.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(cl.id) > 0 then 'true' else 'false' end from clearance cl where cl.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from clearance cl where cl.check_date >= ?1 and cl.check_date <= ?2 and cl.status = 'NEW' and deleted = false", nativeQuery = true)
    ArrayList<DOClearance> getNotificationList(long start, long end);
    
    @Query(value = "select * from clearance where project_id = ?1  and deleted = false", nativeQuery = true)
    DOClearance getItemsByProjectId(String projectId);

    @Transactional
    @Modifying
    @Query(value = "update clearance cl set cl.deleted = ?1 where cl.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);
}
