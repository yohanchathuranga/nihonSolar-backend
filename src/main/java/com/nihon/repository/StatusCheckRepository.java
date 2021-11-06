package com.nihon.repository;

import com.nihon.customrepositories.StatusCheckRepositoryCustom;
import com.nihon.entity.DOStatusCheck;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StatusCheckRepository extends JpaRepository<DOStatusCheck, String>, StatusCheckRepositoryCustom {

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

    @Query(value = "select * from status_check s  where s.actual_date >= ?1 and s.actual_date <= ?2 and s.type ='BANK_LOAN' and s.status = 'NEW' and s.deleted = false", nativeQuery = true)
    ArrayList<DOStatusCheck> getBankLoanNotificationList(long start, long end);
    
    @Query(value = "select * from status_check s  where s.actual_date >= ?1 and s.actual_date <= ?2 and s.type ='CLEARANCE' and s.status = 'NEW' and s.deleted = false", nativeQuery = true)
    ArrayList<DOStatusCheck> getClearancenNotificationList(long start, long end);

    @Query(value = "select * from status_check s  where s.actual_date >= ?1 and s.actual_date <= ?2 and s.type ='INSURANCE' and s.status = 'NEW' and s.deleted = false", nativeQuery = true)
    ArrayList<DOStatusCheck> getInsuranceNotificationList(long start, long end);
    
    @Query(value = "select * from status_check s  where s.actual_date >= ?1 and s.actual_date <= ?2 and s.type =?3 and s.status = 'NEW' and s.deleted = false", nativeQuery = true)
    ArrayList<DOStatusCheck> getNotificationListByType(long start, long end,String type);
    
    
    @Query(value = "select * from status_check where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOStatusCheck> getItemsByProjectId(String projectId);

    @Query(value = "select * from status_check where project_id = ?1 and type = ?2 and deleted = false", nativeQuery = true)
    List<DOStatusCheck> getItemsByProjectIdAndType(String projectId, String type);

    @Transactional
    @Modifying
    @Query(value = "update status_check s set s.deleted = ?1 where s.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);

    @Query(value = "select case when count(s.id) > 0 then 'true' else 'false' end from status_check s where s.project_id = ?1 and type = ?2 and check_no = ?3 and deleted = false", nativeQuery = true)
    boolean isExistsByProjectIdTypeAndWeekNo(String projectId, String type, int weekNo);

    @Query(value = "select COALESCE(MAX(check_no),0) from status_check where project_id = ?1 and type = ?2 and deleted = false", nativeQuery = true)
    int getMaxCheckNoByType(String projectId, String type);
    
    @Transactional
    @Modifying
    @Query(value = "update status_check  set deleted = ?1 where project_id = ?2 and type = ?3 and check_no = ?4", nativeQuery = true)
    int deleteStatusCheckByTypeAndCheckNo(boolean deleted, String projectId, String type, int weekNo);
    
    @Query(value = "select * from status_check s where s.project_id = ?1 and type = ?2 and check_no = ?3 and deleted = false", nativeQuery = true)
    DOStatusCheck getByProjectIdTypeAndWeekNo(String projectId, String type, int weekNo);
}
