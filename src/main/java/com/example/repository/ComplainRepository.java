package com.example.repository;

import com.example.customrepositories.ComplainRepositoryCustom;
import com.example.entity.DOComplain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ComplainRepository extends JpaRepository<DOComplain, String>,ComplainRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update complain c set c.deleted = ?1 where c.id = ?2", nativeQuery = true)
    int deleteComplain(boolean deleted, String id);
    
    @Query(value = "update complain c set c.status = ?1 where c.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(c.id) > 0 then 'true' else 'false' end from complain c where c.id = ?1", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from complain where project_id = ?1", nativeQuery = true)
    List<DOComplain> getItemsByProjectId(String id);


}
