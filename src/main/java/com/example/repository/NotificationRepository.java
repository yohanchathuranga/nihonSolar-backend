package com.example.repository;

import com.example.customrepositories.NotificationRepositoryCustom;
import com.example.entity.DONotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<DONotification, String>,NotificationRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update notification n set n.deleted = ?1 where n.id = ?2", nativeQuery = true)
    int deleteNotification(boolean deleted, String id);
    
    @Query(value = "update notification n set n.status = ?1 where n.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(n.id) > 0 then 'true' else 'false' end from notification n where n.id = ?1", nativeQuery = true)
    boolean isExistsById(String id);


}
