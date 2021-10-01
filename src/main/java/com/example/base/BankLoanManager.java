/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.base;

import com.example.dataaccess.DAODataUtil;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOBankLoan;
import com.example.repository.ProjectRepository;
import com.example.repository.BankLoanRepository;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
import com.example.util.InputValidatorUtil;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;

/**
 *
 * @author yohan
 */
@Service
public class BankLoanManager {

    @Autowired
    private BankLoanRepository bankLoanRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;

    public BankLoanManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.dataUtil = dataUtil;
    }


    public List<DOBankLoan> listBankLoans(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "bank_loan");

            return this.bankLoanRepository.listBankLoans(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countBankLoans(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "bank_loan");
            Object o = this.bankLoanRepository.countBankLoans(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOBankLoan getBankLoanById(String bankLoanId) throws CustomException {
        try {

            bankLoanId = InputValidatorUtil.validateStringProperty("Bank Loan Id", bankLoanId);
            if (!this.bankLoanRepository.isExistsById(bankLoanId)) {
                throw new DoesNotExistException("Bank Loan does not exists. Bank Loan Id : " + bankLoanId);
            }
            return this.bankLoanRepository.findById(bankLoanId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOBankLoan createBankLoan(DOBankLoan bankLoan) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", bankLoan.getProjectId());
            bankLoan.setProjectId(projectId);

            String userId = InputValidatorUtil.validateStringProperty("User Id", bankLoan.getUserId());
            bankLoan.setUserId(userId);
            
            String bankName = InputValidatorUtil.validateStringProperty("Bank Name", bankLoan.getBankName());
            bankLoan.setBankName(bankName);         

            String branch = InputValidatorUtil.validateStringProperty("Branch", bankLoan.getBranch());
            bankLoan.setBranch(branch);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }
            
            String id = UUID.randomUUID().toString();

            bankLoan.setId(id);
            bankLoan.setStatus(DataUtil.QUOTATION_STATE_NEW);
            bankLoan.setDeleted(false);

            DOBankLoan bankLoanCreated = this.bankLoanRepository.save(bankLoan);
            return bankLoanCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteBankLoan(String bankLoanId) throws CustomException {
        try {

            bankLoanId = InputValidatorUtil.validateStringProperty("Bank Loan Id", bankLoanId);
            if (!this.bankLoanRepository.isExistsById(bankLoanId)) {
                throw new DoesNotExistException("Bank Loan does not exists.Bank Loan Id : " + bankLoanId);
            }
            this.bankLoanRepository.deleteBankLoan(true, bankLoanId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOBankLoan updateBankLoan(DOBankLoan bankLoan) throws CustomException {
        try {

            String bankLoanId =InputValidatorUtil.validateStringProperty("Bank Loan Id", bankLoan.getId());
            bankLoan.setId(bankLoanId);
            
            String projectId = InputValidatorUtil.validateStringProperty("Project Id", bankLoan.getProjectId());
            bankLoan.setProjectId(projectId);

            String userId = InputValidatorUtil.validateStringProperty("User Id", bankLoan.getUserId());
            bankLoan.setUserId(userId);
            
            String bankName = InputValidatorUtil.validateStringProperty("Bank Name", bankLoan.getBankName());
            bankLoan.setBankName(bankName);         

            String branch = InputValidatorUtil.validateStringProperty("Branch", bankLoan.getBranch());
            bankLoan.setBranch(branch);
            
            if (!this.bankLoanRepository.isExistsById(bankLoanId)) {
                throw new DoesNotExistException("Bank loan does not exists. Bank loan Id : " + bankLoanId);
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            bankLoan.setStatus(DataUtil.QUOTATION_STATE_NEW);
            bankLoan.setDeleted(false);

            DOBankLoan bankLoanCreated = this.bankLoanRepository.save(bankLoan);
            return bankLoanCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
