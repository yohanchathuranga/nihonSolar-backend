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
import com.nihon.entity.DOPayment;
import com.nihon.entity.DOProject;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.PaymentRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.InputValidatorUtil;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import yohan.exceptions.InvalidInputException;
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

            DOProject project = projectRepository.getById(projectId);
            long date = payment.getDate();
            if (date <= 0) {
                throw new InvalidInputException("Invalid date. Date : " + date);
            }
            payment.setDate(date);

            int amount = payment.getAmount();
            if (amount <= 0) {
                throw new InvalidInputException("Invalid Amount. Amount : " + amount);
            }
            if (amount > project.getOutstandingPayment()) {
                throw new InvalidInputException("Amount is greater than outstanding payment. Amount : " + amount);
            }
            payment.setAmount(amount);

            String type = InputValidatorUtil.validateStringProperty("Type", payment.getType());
            payment.setType(type);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            String id = UUID.randomUUID().toString();

            payment.setId(id);
            payment.setDeleted(false);

            DOPayment paymentCreated = this.paymentRepository.save(payment);

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

    @Transactional
    public DOPayment updatePayment(DOPayment payment) throws CustomException {
        try {
            String paymentId = InputValidatorUtil.validateStringProperty("Payment Id", payment.getId());
            payment.setId(paymentId);

            if (!this.paymentRepository.isExistsById(paymentId)) {
                throw new DoesNotExistException("Payment does not exists.Payment Id : " + paymentId);
            }

            DOPayment paymentExists = paymentRepository.findById(paymentId).get();

            String projectId = paymentExists.getProjectId();

            DOProject project = projectRepository.getById(projectId);

            long date = payment.getDate();
            if (date <= 0) {
                throw new InvalidInputException("Invalid date. Date : " + date);
            }
            payment.setDate(date);

            int amount = payment.getAmount();
            if (amount <= 0) {
                throw new InvalidInputException("Invalid Amount. Amount : " + amount);
            }
            if (amount > (project.getOutstandingPayment() + paymentExists.getAmount())) {
                throw new InvalidInputException("Amount is greater than outstanding payment. Amount : " + amount);
            }
            payment.setAmount(amount);

            String type = InputValidatorUtil.validateStringProperty("Type", payment.getType());
            payment.setType(type);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            payment.setProjectId(projectId);
            payment.setDeleted(false);

            DOPayment paymentUpdated = this.paymentRepository.save(payment);

            project.setOutstandingPayment(project.getOutstandingPayment() + paymentExists.getAmount() - amount);
            this.projectRepository.save(project);
            return paymentUpdated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
