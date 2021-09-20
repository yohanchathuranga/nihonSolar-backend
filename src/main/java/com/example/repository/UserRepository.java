package com.example.repository;

import com.example.customrepositories.UserRepositoryCustom;
import com.example.entity.DOUser;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<DOUser, String>,UserRepositoryCustom {
    
    @Query(value = "SELECT o.*,u.first_name as firstName,u.last_name as lastName,o.user_id as userId ,u.email FROM orders o join users u on o.user_id = u.id WHERE o.id = ?1", nativeQuery = true)
    Or getOrder(int orderId);

    
    public static interface Or{
        int getId();
        String getProduct();
        int getQuantity();
        String getUserId();
        String getFirstName();
        String getLastName();
        String getEmail();
    }
    
    @Transactional
    @Modifying
    @Query(value = "update users u set u.deleted = ?1 where u.id = ?2", nativeQuery = true)
    int deleteUser(boolean deleted, String id);
    
    @Query(value = "update users u set u.status = ?1 where u.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(u.id) > 0 then 'true' else 'false' end from users u where u.id = ?1", nativeQuery = true)
    boolean isExistsById(String id);
    
    @Query(value = "insert into customer (user_id,occupation) values (?1,?2)", nativeQuery = true)
    int insertCustomer(String userId, String occupation);
    
    @Query(value = "insert into employee (user_id,role) values (?1,?2)", nativeQuery = true)
    int insertEmployee(String userId, String role);
    
    @Query(value = "select occupation from customer where user_id = 1?", nativeQuery = true)
    DOUser getCustomer(String userId);
    
    @Query(value = "select role from employee where user_id = 1?", nativeQuery = true)
    DOUser getEmployee(String userId);

    @Query(value = "select * from users u where u.occupation = ?1 and u.type = 'EMPLOYEE'", nativeQuery = true)
    ArrayList<DOUser> getEmployeeByRole(String role);
    
    @Query(value = "select * from users where id = ?1", nativeQuery = true)
    DOUser getById(String id);
}
