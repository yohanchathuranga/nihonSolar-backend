package com.nihon.repository;

import com.nihon.customrepositories.ProjectRepositoryCustom;
import com.nihon.entity.DOProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectRepository extends JpaRepository<DOProject, String>, ProjectRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "update project p set p.deleted = ?1 where p.id = ?2", nativeQuery = true)
    int deleteProject(boolean deleted, String id);

    @Transactional
    @Modifying
    @Query(value = "update project p set p.status = ?1 where p.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(p.id) > 0 then 'true' else 'false' end from project p where p.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

//    @Query(value = "insert into customer (user_id,occupation) values (?1,?2)", nativeQuery = true)
//    int insertCustomer(String userId, String occupation);
//    
//    @Query(value = "insert into employee (user_id,role) values (?1,?2)", nativeQuery = true)
//    int insertEmployee(String userId, String role);
//    
//    @Query(value = "select occupation from customer where user_id = 1?", nativeQuery = true)
//    DOProject getCustomer(String userId);
//    
//    @Query(value = "select role from employee where user_id = 1?", nativeQuery = true)
//    DOProject getEmployee(String userId);
    @Query(value = "select * from project p where p.id = ?1 and deleted = false", nativeQuery = true)
    DOProject getById(String id);

    @Query(value = "select case when count(p.id) > 0 then 'true' else 'false' end from project p where p.id = ?1 and p.user_id = ?2 and deleted = false and deleted = false", nativeQuery = true)
    boolean isExistsByIdandUserId(String id, String userId);
    
    @Query(value = "select case when count(p.id) > 0 then 'true' else 'false' end from project p where p.id = ?1 and deleted = false and status <> 'CANCELLED'", nativeQuery = true)
    boolean checkProjectAlive(String id);
}
