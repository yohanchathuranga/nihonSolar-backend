package com.nihon.repository;

import com.nihon.customrepositories.UserRepositoryCustom;
import com.nihon.entity.DOProjectUser;
import com.nihon.entity.DOUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<DOUser, String>, UserRepositoryCustom {

    @Query(value = "SELECT u.id,u.first_name as firstName,u.last_name as lastName,u.project_id as projectId ,u.email,u.contact_no as contactNo,u.address,u.occupation,u.nic,u.status,u.type FROM view_users u where u.first_name like ?1 order by u.project_created_date limit ?2 offset ?3 ", nativeQuery = true)
    List<DOProjectUser> getCustomers(String like,int limit ,int offset);

    public static interface Or {

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

    @Transactional
    @Modifying
    @Query(value = "update users u set u.status = ?1 where u.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(u.id) > 0 then 'true' else 'false' end from users u where u.id = ?1 and u.deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from users u where u.occupation = ?1 and u.type = 'EMPLOYEE' and deleted = false", nativeQuery = true)
    ArrayList<DOUser> getEmployeeByRole(String role);

    @Query(value = "select * from users where id = ?1 and deleted = false", nativeQuery = true)
    DOUser getById(String id);

    @Query(value = "select case when count(u.id) > 0 then 'true' else 'false' end from users u where u.email = ?1 and u.deleted = false", nativeQuery = true)
    boolean isExistsByEmail(String email);

    @Query(value = "select * from users where email = ?1 and deleted = false", nativeQuery = true)
    DOUser getByEmail(String email);

    @Query(value = "select case when count(user_id) > 0 then 'true' else 'false' end from forgot_password  where user_id = ?1", nativeQuery = true)
    boolean isForgetPasswordExistsByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "insert into forgot_password (user_id,email,verification_code) values(?1,?2,?3)", nativeQuery = true)
    int createForgotPassword(String userId, String email, String verificationCode);

    @Transactional
    @Modifying
    @Query(value = "update forgot_password set verification_code = ?1 where user_id = ?2", nativeQuery = true)
    int updateForgotPassword(String verificationCode, String userId);
    
    @Query(value = "select verification_code from forgot_password where user_id = ?1", nativeQuery = true)
    String getForgotPassword(String userId);
}
