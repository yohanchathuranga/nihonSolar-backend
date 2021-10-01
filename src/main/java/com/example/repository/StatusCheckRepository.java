package com.example.repository;

import com.example.customrepositories.StatusCheckRepositoryCustom;
import com.example.entity.DOStatusCheck;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StatusCheckRepository extends JpaRepository<DOStatusCheck, String>,StatusCheckRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update status_check s set s.deleted = ?1 where s.id = ?2", nativeQuery = true)
    int deleteStatusCheck(boolean deleted, String id);
    
    @Transactional
    @Modifying
    @Query(value = "update status_check s set s.status = ?1 where s.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(s.id) > 0 then 'true' else 'false' end from status_check s where s.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from status_check s  where s.actual_date >= ?1 and s.actual_date <= ?2 and s.status = 'NEW' and deleted = false", nativeQuery = true)
    ArrayList<DOStatusCheck> getNotificationList(long start, long end);
    
    @Query(value = "select * from status_check where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOStatusCheck> getItemsByProjectId(String projectId);

}
