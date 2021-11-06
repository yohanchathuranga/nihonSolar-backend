package com.nihon.repository;

import com.nihon.customrepositories.ProjectQuotationRepositoryCustom;
import com.nihon.entity.DOProjectQuotation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectQuotationRepository extends JpaRepository<DOProjectQuotation, String>,ProjectQuotationRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update project_quotation q set q.deleted = ?1 where q.id = ?2", nativeQuery = true)
    int deleteProjectQuotation(boolean deleted, String id);
    

    @Query(value = "select case when count(q.id) > 0 then 'true' else 'false' end from project_quotation q where q.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);
    
    @Query(value = "select * from project_quotation where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOProjectQuotation> getItemsByProjectId(String projectId);
    
    @Query(value = "select * from project_quotation where id = ?1 and deleted = false", nativeQuery = true)
    DOProjectQuotation getItemsById(String id);

    @Transactional
    @Modifying
    @Query(value = "update project_quotation q set q.deleted = ?1 where q.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);
    
    @Transactional
    @Modifying
    @Query(value = "update project_quotation q set q.selected = ?1 where q.id = ?2", nativeQuery = true)
    int setSelectQuotation(boolean deleted, String id);
}
