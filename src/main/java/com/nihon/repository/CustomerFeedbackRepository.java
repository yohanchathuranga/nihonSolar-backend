package com.nihon.repository;

import com.nihon.customrepositories.CustomerFeedbackRepositoryCustom;
import com.nihon.entity.DOCustomerFeedback;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerFeedbackRepository extends JpaRepository<DOCustomerFeedback, String>, CustomerFeedbackRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "update customer_feedback cf set cf.deleted = ?1 where cf.id = ?2", nativeQuery = true)
    int deleteCustomerFeedback(boolean deleted, String id);

    @Transactional
    @Modifying
    @Query(value = "update customer_feedback cf set cf.status = ?1 where cf.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(cf.id) > 0 then 'true' else 'false' end from customer_feedback cf where cf.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from customer_feedback cf where cf.actual_date >= ?1 and cf.actual_date < ?2 and cf.status = 'NEW' and cf.deleted = false", nativeQuery = true)
    ArrayList<DOCustomerFeedback> getNotificationList(long start, long end);

    @Query(value = "select * from customer_feedback where project_id = ?1 and deleted = false", nativeQuery = true)
    List<DOCustomerFeedback> getItemsByProjectId(String projectId);

    @Transactional
    @Modifying
    @Query(value = "update customer_feedback cf set cf.deleted = ?1 where cf.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);

    @Query(value = "select case when count(cf.id) > 0 then 'true' else 'false' end from customer_feedback cf where cf.project_id = ?1 and week_no = ?2 and deleted = false", nativeQuery = true)
    boolean isExistsByProjectIdAndWeekNo(String projectId, int weekNo);
    
    @Query(value = "select COALESCE(MAX(week_no),0) from customer_feedback where project_id = ?1 and deleted = false", nativeQuery = true)
    int getMaxCheckNoByType(String projectId);
}
