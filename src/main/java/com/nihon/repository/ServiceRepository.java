package com.nihon.repository;

import com.nihon.customrepositories.ServiceRepositoryCustom;
import com.nihon.entity.DOService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ServiceRepository extends JpaRepository<DOService, String>,ServiceRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update service s set s.deleted = ?1 where s.id = ?2", nativeQuery = true)
    int deleteService(boolean deleted, String id);
    
    @Transactional
    @Modifying
    @Query(value = "update service s set s.status = ?1 where s.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(s.id) > 0 then 'true' else 'false' end from service s where s.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from service where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOService> getItemsByProjectId(String projectId);

    @Transactional
    @Modifying
    @Query(value = "update service s set s.deleted = ?1 where s.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);
    
    @Query(value = "select COALESCE(MAX(service_no),0) from service where project_id = ?1 and deleted = false", nativeQuery = true)
    int getMaxCheckNo(String projectId);
}
