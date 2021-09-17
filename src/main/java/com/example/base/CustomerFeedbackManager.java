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
import com.example.entity.DOCustomerFeedback;
import com.example.repository.ProjectRepository;
import com.example.repository.CustomerFeedbackRepository;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
import com.example.util.DateTimeUtil;
import com.example.util.InputValidatorUtil;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.DoesNotExistException;
import com.yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yohan
 */
@Service
public class CustomerFeedbackManager {

    @Autowired
    private CustomerFeedbackRepository customerFeedbackRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;

    public CustomerFeedbackManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOCustomerFeedback> listCustomerFeedbacks(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "customer_feedback");

            return this.customerFeedbackRepository.listCustomerFeedbacks(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countCustomerFeedbacks(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "customer_feedback");
            Object o = this.customerFeedbackRepository.countCustomerFeedbacks(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOCustomerFeedback getCustomerFeedbackById(String customerFeedbackId) throws CustomException {
        try {

            customerFeedbackId = InputValidatorUtil.validateStringProperty("Customer Feedback Id", customerFeedbackId);
            if (!this.customerFeedbackRepository.isExistsById(customerFeedbackId)) {
                throw new DoesNotExistException("Customer Feedback does not exists. Customer Feedback Id : " + customerFeedbackId);
            }
            return this.customerFeedbackRepository.findById(customerFeedbackId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOCustomerFeedback createCustomerFeedback(DOCustomerFeedback customerFeedback) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", customerFeedback.getProjectId());
            customerFeedback.setProjectId(projectId);

            String customerId = InputValidatorUtil.validateStringProperty("Customer Id", customerFeedback.getCustomerId());
            customerFeedback.setCustomerId(customerId);

            int weekNo = customerFeedback.getWeekNo();
            if (weekNo < 1) {
                throw new InvalidInputException("Invalid Week No.Week No : " + weekNo);
            }
            customerFeedback.setWeekNo(weekNo);
            
            long acctualDate = customerFeedback.getActualDate();
            if (acctualDate < 1) {
                throw new InvalidInputException("Invalid Actual Date. Actual Date : " + acctualDate);
            }
            customerFeedback.setActualDate(acctualDate);

            if (!this.userRepository.isExistsById(customerId)) {
                throw new DoesNotExistException("Customer does not exists. Customer Id : " + customerId);
            }
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            String id = UUID.randomUUID().toString();

            customerFeedback.setId(id);
            customerFeedback.setStatus(DataUtil.FEEDBACK_STATE_NEW);
            customerFeedback.setDeleted(false);

            DOCustomerFeedback customerFeedbackCreated = this.customerFeedbackRepository.save(customerFeedback);
            return customerFeedbackCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteCustomerFeedback(String customerFeedbackId) throws CustomException {
        try {

            customerFeedbackId = InputValidatorUtil.validateStringProperty("Customer Feedback Id", customerFeedbackId);
            if (!this.customerFeedbackRepository.isExistsById(customerFeedbackId)) {
                throw new DoesNotExistException("Customer Feedback does not exists.Customer Feedback Id : " + customerFeedbackId);
            }
            this.customerFeedbackRepository.deleteCustomerFeedback(true, customerFeedbackId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOCustomerFeedback updateCustomerFeedback(DOCustomerFeedback customerFeedback) throws CustomException {
        try {
            String customerFeedbackId = InputValidatorUtil.validateStringProperty("Customer feedback Id", customerFeedback.getId());
            customerFeedback.setId(customerFeedbackId);
            
            String projectId = InputValidatorUtil.validateStringProperty("Project Id", customerFeedback.getProjectId());
            customerFeedback.setProjectId(projectId);

            String customerId = InputValidatorUtil.validateStringProperty("Customer Id", customerFeedback.getCustomerId());
            customerFeedback.setCustomerId(customerId);

            int weekNo = customerFeedback.getWeekNo();
            if (weekNo < 1) {
                throw new InvalidInputException("Invalid Week No.Week No : " + weekNo);
            }
            customerFeedback.setWeekNo(weekNo);
            
            long acctualDate = customerFeedback.getActualDate();
            if (acctualDate < 1) {
                throw new InvalidInputException("Invalid Actual Date. Actual Date : " + acctualDate);
            }
            customerFeedback.setActualDate(acctualDate);

            if (!this.customerFeedbackRepository.isExistsById(customerFeedbackId)) {
                throw new DoesNotExistException("Customer Feedback does not exists.Customer Feedback Id : " + customerFeedbackId);
            }
            
            if (!this.userRepository.isExistsById(customerId)) {
                throw new DoesNotExistException("Customer does not exists. Customer Id : " + customerId);
            }
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            customerFeedback.setStatus(DataUtil.QUOTATION_STATE_NEW);
            customerFeedback.setDeleted(false);

            DOCustomerFeedback customerFeedbackCreated = this.customerFeedbackRepository.save(customerFeedback);
            return customerFeedbackCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
