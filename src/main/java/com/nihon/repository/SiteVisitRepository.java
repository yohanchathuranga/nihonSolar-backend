package com.nihon.repository;

import com.nihon.customrepositories.SiteVisitRepositoryCustom;
import com.nihon.entity.DOSiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SiteVisitRepository extends JpaRepository<DOSiteVisit, String>,SiteVisitRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update site_visit s set s.deleted = ?1 where s.id = ?2", nativeQuery = true)
    int deleteSiteVisit(boolean deleted, String id);
    
    @Transactional
    @Modifying
    @Query(value = "update site_vist s set s.status = ?1 where s.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(s.id) > 0 then 'true' else 'false' end from site_visit s where s.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from site_visit where project_id = ?1 and deleted = false limit 1", nativeQuery = true)
    DOSiteVisit getItemsByProjectId(String projectId);
    
    @Transactional
    @Modifying
    @Query(value = "update site_visit s set s.deleted = ?1 where s.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);
}
