package com.nihon.repository;

import com.nihon.customrepositories.QuotationRepositoryCustom;
import com.nihon.entity.DOQuotation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface QuotationRepository extends JpaRepository<DOQuotation, String>,QuotationRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update quotation q set q.deleted = ?1 where q.id = ?2", nativeQuery = true)
    int deleteQuotation(boolean deleted, String id);
    
    @Transactional
    @Modifying
    @Query(value = "update quotation q set q.status = ?1 where q.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(q.id) > 0 then 'true' else 'false' end from quotation q where q.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);
    
    @Query(value = "select * from quotation where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOQuotation> getItemsByProjectId(String projectId);
    
    @Query(value = "select * from quotation where id = ?1 and deleted = false", nativeQuery = true)
    DOQuotation getItemsById(String id);

    @Transactional
    @Modifying
    @Query(value = "update quotation q set q.deleted = ?1 where q.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);
}
