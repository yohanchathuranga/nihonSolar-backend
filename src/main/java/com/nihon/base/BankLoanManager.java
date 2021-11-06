/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.base;

import com.nihon.dataaccess.DAODataUtil;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOBankLoan;
import com.nihon.entity.DOStatusCheck;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.BankLoanRepository;
import com.nihon.repository.StatusCheckRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.DateTimeUtil;
import com.nihon.util.InputValidatorUtil;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yohan.exceptions.AlreadyExistException;
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
    private StatusCheckRepository statusCheckRepository;
    private final DAODataUtil dataUtil;

    public BankLoanManager(ProjectRepository projectRepository, UserRepository userRepository, StatusCheckRepository statusCheckRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.statusCheckRepository = statusCheckRepository;
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

    @Transactional
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

            if (!this.projectRepository.isExistsByIdandUserId(projectId, userId)) {
                throw new DoesNotExistException("Project does not belongs to customer. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            if (bankLoanRepository.getItemsByProjectId(projectId) != null) {
                throw new AlreadyExistException("Already exists for Projrct id . Project Id :" + projectId);
            }

            if (bankLoan.getSubmitDate() <= 0) {
                bankLoan.setSubmitDate(DateTimeUtil.getCurrentTime());
            }
            long nextWeek = bankLoan.getSubmitDate();
            DOStatusCheck statusCheck = new DOStatusCheck();
            statusCheck.setProjectId(projectId);
            statusCheck.setType(DataUtil.STATUS_CHECK_TYPE_BANK_LOAN);
            statusCheck.setStatus(DataUtil.STATUS_CHECK_STATE_NEW);
            statusCheck.setDeleted(false);
            for (int i = 1; i < 5; i++) {
                statusCheck.setId(UUID.randomUUID().toString());
                statusCheck.setCheckNo(i);
                statusCheck.setActualDate(DateTimeUtil.getNextWeekDayTime(nextWeek));
                statusCheckRepository.save(statusCheck);
                nextWeek = DateTimeUtil.getNextWeekDayTime(nextWeek);
            }
            String id = UUID.randomUUID().toString();

            bankLoan.setId(id);
            bankLoan.setStatus(DataUtil.BANK_LOAN_STATUS_NEW);
            bankLoan.setDeleted(false);

            DOBankLoan bankLoanCreated = this.bankLoanRepository.save(bankLoan);
            return bankLoanCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public boolean deleteBankLoan(String bankLoanId) throws CustomException {
        try {

            bankLoanId = InputValidatorUtil.validateStringProperty("Bank Loan Id", bankLoanId);
            if (!this.bankLoanRepository.isExistsById(bankLoanId)) {
                throw new DoesNotExistException("Bank Loan does not exists.Bank Loan Id : " + bankLoanId);
            }
            DOBankLoan bankLoan = bankLoanRepository.findById(bankLoanId).get();

            List<DOStatusCheck> statusChecks = statusCheckRepository.getItemsByProjectIdAndType(bankLoan.getProjectId(), DataUtil.STATUS_CHECK_TYPE_BANK_LOAN);
            for (DOStatusCheck statusCheck : statusChecks) {
                statusCheckRepository.deleteStatusCheck(true, statusCheck.getId());
            }

            this.bankLoanRepository.deleteBankLoan(true, bankLoanId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOBankLoan updateBankLoan(DOBankLoan bankLoan) throws CustomException {
        try {

            String bankLoanId = InputValidatorUtil.validateStringProperty("Bank Loan Id", bankLoan.getId());
            bankLoan.setId(bankLoanId);

            if (!this.bankLoanRepository.isExistsById(bankLoanId)) {
                throw new DoesNotExistException("Bank loan does not exists. Bank loan Id : " + bankLoanId);
            }

            DOBankLoan bankLoanExists = bankLoanRepository.findById(bankLoanId).get();

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", bankLoanExists.getProjectId());
            bankLoan.setProjectId(projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }
            bankLoanExists.setBranch(bankLoan.getBranch());
            bankLoanExists.setOfficer(bankLoan.getOfficer());
            bankLoanExists.setContactNo(bankLoan.getContactNo());
            DOBankLoan bankLoanCreated = this.bankLoanRepository.save(bankLoanExists);
            return bankLoanCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public DOBankLoan approve(DOBankLoan bankLoan) throws CustomException {
        try {

            String bankLoanId = InputValidatorUtil.validateStringProperty("Bank Loan Id", bankLoan.getId());

            if (!this.bankLoanRepository.isExistsById(bankLoanId)) {
                throw new DoesNotExistException("Bank loan does not exists. Bank loan Id : " + bankLoanId);
            }

            if (bankLoan.getApprovedDate() <= 0) {
                bankLoan.setApprovedDate(DateTimeUtil.getCurrentTime());
            }

            DOBankLoan bankLoanExists = bankLoanRepository.findById(bankLoanId).get();

            List<DOStatusCheck> statusChecks = statusCheckRepository.getItemsByProjectIdAndType(bankLoanExists.getProjectId(), DataUtil.STATUS_CHECK_TYPE_BANK_LOAN);
            for (DOStatusCheck statusCheck : statusChecks) {
                if (statusCheck.getStatus().equals(DataUtil.STATUS_CHECK_STATE_NEW)) {
                    statusCheckRepository.setStatus(DataUtil.STATE_DISCARD, statusCheck.getId());
                }
            }

            bankLoanExists.setApprovedDate(bankLoan.getApprovedDate());
            bankLoanExists.setStatus(DataUtil.BANK_LOAN_STATUS_APPROVED);
            bankLoanExists.setDeleted(false);

            DOBankLoan bankLoanCreated = this.bankLoanRepository.save(bankLoanExists);
            return bankLoanCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public DOBankLoan cancel(String bankLoanId) throws CustomException {
        try {

            bankLoanId = InputValidatorUtil.validateStringProperty("Bank Loan Id", bankLoanId);

            if (!this.bankLoanRepository.isExistsById(bankLoanId)) {
                throw new DoesNotExistException("Bank loan does not exists. Bank loan Id : " + bankLoanId);
            }

            DOBankLoan bankLoan = bankLoanRepository.findById(bankLoanId).get();

            List<DOStatusCheck> statusChecks = statusCheckRepository.getItemsByProjectIdAndType(bankLoan.getProjectId(), DataUtil.STATUS_CHECK_TYPE_BANK_LOAN);
            for (DOStatusCheck statusCheck : statusChecks) {
                statusCheckRepository.setStatus(DataUtil.STATE_CANCELLED, statusCheck.getId());

            }

            bankLoan.setStatus(DataUtil.BANK_LOAN_STATUS_CANCELLED);
            bankLoan.setDeleted(false);

            DOBankLoan bankLoanCreated = this.bankLoanRepository.save(bankLoan);
            return bankLoanCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
