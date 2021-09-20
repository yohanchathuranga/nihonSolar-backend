package com.example.repository;

import com.example.customrepositories.BankLoanRepositoryCustom;
import com.example.entity.DOBankLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BankLoanRepository extends JpaRepository<DOBankLoan, String>,BankLoanRepositoryCustom {
    
    @Transactional
    @Modifying
    @Query(value = "update bank_loan bl set bl.deleted = ?1 where bl.id = ?2", nativeQuery = true)
    int deleteBankLoan(boolean deleted, String id);
    
    @Query(value = "update bank_loan bl set bl.status = ?1 where bl.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select case when count(bl.id) > 0 then 'true' else 'false' end from bank_loan bl where bl.id = ?1", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from bank_loan where project_id = ?1 limit 1", nativeQuery = true)
    DOBankLoan getItemsByProjectId(String projectId);

}
