package com.nihon.repository;

import com.nihon.customrepositories.InsuranceRepositoryCustom;
import com.nihon.entity.DOInsurance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InsuranceRepository extends JpaRepository<DOInsurance, String>, InsuranceRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "update insurance i set i.deleted = ?1 where i.id = ?2", nativeQuery = true)
    int deleteInsurance(boolean deleted, String id);

    @Transactional
    @Modifying
    @Query(value = "update insurance i set i.status = ?1 where i.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(i.id) > 0 then 'true' else 'false' end from insurance i where i.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from insurance where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOInsurance> getItemsByProjectId(String projectId);

    ;

    @Transactional
    @Modifying
    @Query(value = "update insurance i set i.deleted = ?1 where i.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);

    @Query(value = "select case when count(i.id) > 0 then 'true' else 'false' end from insurance i where i.project_id = ?1 and i.year =?2 and deleted = false", nativeQuery = true)
    boolean isExistsByProjectIdAndYear(String projectId, String year);
}
