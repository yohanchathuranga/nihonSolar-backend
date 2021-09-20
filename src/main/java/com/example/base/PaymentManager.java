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
import com.example.entity.DOPayment;
import com.example.entity.DOProject;
import com.example.repository.ProjectRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.UserRepository;
import com.example.util.InputValidatorUtil;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.DoesNotExistException;
import com.yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yohan
 */
@Service
public class PaymentManager {

    @Autowired
    private PaymentRepository paymentRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;

    public PaymentManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOPayment> listPayments(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "payment");

            return this.paymentRepository.listPayments(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countPayments(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "payment");
            Object o = this.paymentRepository.countPayments(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOPayment getPaymentById(String paymentId) throws CustomException {
        try {

            paymentId = InputValidatorUtil.validateStringProperty("Payment Id", paymentId);
            if (!this.paymentRepository.isExistsById(paymentId)) {
                throw new DoesNotExistException("Payment does not exists. Payment Id : " + paymentId);
            }
            return this.paymentRepository.findById(paymentId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public DOPayment createPayment(DOPayment payment) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", payment.getProjectId());
            payment.setProjectId(projectId);

            long date = payment.getDate();
            if (date <= 0) {
                throw new InvalidInputException("Invalid date. Date : " + date);
            }
            payment.setDate(date);

            int amount = payment.getAmount();
            if (amount <= 0) {
                throw new InvalidInputException("Invalid Amount. Amount : " + amount);
            }
            payment.setAmount(amount);

            String type = InputValidatorUtil.validateStringProperty("Type", payment.getType());
            payment.setType(type);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            String id = UUID.randomUUID().toString();

            payment.setId(id);
            payment.setDeleted(false);

            DOPayment paymentCreated = this.paymentRepository.save(payment);
            
            DOProject project = projectRepository.getById(projectId);
            project.setOutstandingPayment(project.getOutstandingPayment() - amount);
            this.projectRepository.save(project);
            
            return paymentCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public boolean deletePayment(String paymentId) throws CustomException {
        try {

            paymentId = InputValidatorUtil.validateStringProperty("Payment Id", paymentId);
            if (!this.paymentRepository.isExistsById(paymentId)) {
                throw new DoesNotExistException("Payment does not exists.Payment Id : " + paymentId);
            }
            this.paymentRepository.deletePayment(true, paymentId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOPayment updatePayment(DOPayment payment) throws CustomException {
        try {
            String paymentId = InputValidatorUtil.validateStringProperty("Payment Id", payment.getId());
            payment.setId(paymentId);

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", payment.getProjectId());
            payment.setProjectId(projectId);

            long date = payment.getDate();
            if (date <= 0) {
                throw new InvalidInputException("Invalid date. Date : " + date);
            }
            payment.setDate(date);

            int amount = payment.getAmount();
            if (amount <= 0) {
                throw new InvalidInputException("Invalid Amount. Amount : " + amount);
            }
            payment.setAmount(amount);

            String type = InputValidatorUtil.validateStringProperty("Type", payment.getType());
            payment.setType(type);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            
            if (!this.paymentRepository.isExistsById(paymentId)) {
                throw new DoesNotExistException("Payment does not exists.Payment Id : " + paymentId);
            }

            payment.setDeleted(false);

            DOPayment paymentCreated = this.paymentRepository.save(payment);
            return paymentCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
