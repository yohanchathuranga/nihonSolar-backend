/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.base;

import com.nihon.dataaccess.DAODataUtil;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOCustomerFeedback;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOProject;
import com.nihon.entity.DOQuotation;
import com.nihon.repository.CustomerFeedbackRepository;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.QuotationRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.DateTimeUtil;
import com.nihon.util.InputValidatorUtil;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
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
public class QuotationManager {

    @Autowired
    private QuotationRepository quotationRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private CustomerFeedbackRepository customerFeedbackRepository;
    private final DAODataUtil dataUtil;

    public QuotationManager(ProjectRepository projectRepository, UserRepository userRepository, CustomerFeedbackRepository customerFeedbackRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.customerFeedbackRepository = customerFeedbackRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOQuotation> listQuotations(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "quotation");

            return this.quotationRepository.listQuotations(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countQuotations(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "quotation");
            Object o = this.quotationRepository.countQuotations(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOQuotation getQuotationById(String quotationId) throws CustomException {
        try {

            quotationId = InputValidatorUtil.validateStringProperty("Quotation Id", quotationId);
            if (!this.quotationRepository.isExistsById(quotationId)) {
                throw new DoesNotExistException("Quotation does not exists. Quotation Id : " + quotationId);
            }
            return this.quotationRepository.findById(quotationId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public DOQuotation createQuotation(DOQuotation quotation) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", quotation.getProjectId());
            quotation.setProjectId(projectId);

            String userId = InputValidatorUtil.validateStringProperty("User Id", quotation.getUserId());
            quotation.setUserId(userId);

            long currentTime = DateTimeUtil.getCurrentTime();
            quotation.setIssuedDate(currentTime);

            String sendMode = InputValidatorUtil.validateStringProperty("Send Mode", quotation.getSendMode());
            quotation.setSendMode(sendMode);

            String inventerType = InputValidatorUtil.validateStringProperty("Inventor Type", quotation.getInventerType());
            quotation.setInventerType(inventerType);

            String panelType = InputValidatorUtil.validateStringProperty("Panel Type", quotation.getPanelType());
            quotation.setPanelType(panelType);

            String acDcSpd = InputValidatorUtil.validateStringProperty("AC DC SPD", quotation.getAcDcSpd());
            quotation.setAcDcSpd(acDcSpd);

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.isExistsByIdandUserId(projectId, userId)) {
                throw new DoesNotExistException("Project does not belongs to customer. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }
            
            List<DOQuotation> quotations = this.quotationRepository.getItemsByProjectId(quotation.getProjectId());
            if (quotations.isEmpty()) {
                long nextWeek = currentTime;
                DOCustomerFeedback customerFeedback = new DOCustomerFeedback();
                customerFeedback.setProjectId(projectId);
                customerFeedback.setCustomerId(userId);
                customerFeedback.setStatus(DataUtil.FEEDBACK_STATE_NEW);
                customerFeedback.setDeleted(false);
                for (int i = 1; i < 5; i++) {
                    customerFeedback.setId(UUID.randomUUID().toString());
                    customerFeedback.setWeekNo(i);
                    customerFeedback.setActualDate(DateTimeUtil.getNextWeekDayTime(nextWeek));
                    customerFeedbackRepository.save(customerFeedback);
                    nextWeek = DateTimeUtil.getNextWeekDayTime(nextWeek);
                }
                
            }

            projectRepository.setStatus(DataUtil.PROJECT_STATE_PENDING, projectId);
            String id = UUID.randomUUID().toString();

            quotation.setId(id);
            quotation.setStatus(DataUtil.QUOTATION_STATE_NEW);
            quotation.setDeleted(false);

            DOQuotation quotationCreated = this.quotationRepository.save(quotation);
            return quotationCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteQuotation(String quotationId) throws CustomException {
        try {

            quotationId = InputValidatorUtil.validateStringProperty("Quotation Id", quotationId);
            if (!this.quotationRepository.isExistsById(quotationId)) {
                throw new DoesNotExistException("Quotation does not exists.Quotation Id : " + quotationId);
            }
            this.quotationRepository.deleteQuotation(true, quotationId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOQuotation updateQuotation(DOQuotation quotation) throws CustomException {
        try {
            String quotationId = InputValidatorUtil.validateStringProperty("Quotation Id", quotation.getId());
            quotation.setId(quotationId);

            DOQuotation quotationExists = quotationRepository.getItemsById(quotationId);
            
            String projectId = quotationExists.getProjectId();

            String userId = quotationExists.getUserId();

            String sendMode = InputValidatorUtil.validateStringProperty("Send Mode", quotation.getSendMode());
            quotation.setSendMode(sendMode);

            String inventerType = InputValidatorUtil.validateStringProperty("Inventor Type", quotation.getInventerType());
            quotation.setInventerType(inventerType);

            String panelType = InputValidatorUtil.validateStringProperty("Panel Type", quotation.getPanelType());
            quotation.setPanelType(panelType);

            String acDcSpd = InputValidatorUtil.validateStringProperty("AC DC SPD", quotation.getAcDcSpd());
            quotation.setAcDcSpd(acDcSpd);

            if (!this.quotationRepository.isExistsById(quotationId)) {
                throw new DoesNotExistException("Quotation does not exists.Quotation Id : " + quotationId);
            }

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.isExistsByIdandUserId(projectId, userId)) {
                throw new DoesNotExistException("Project does not belongs to customer. Project Id : " + projectId);
            }
            
            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }
            

            quotation.setProjectId(projectId);
            quotation.setUserId(userId);
            quotation.setIssuedDate(quotationExists.getIssuedDate());
            if(quotation.getStatus().isEmpty() || quotation.getStatus() ==null){
                quotation.setStatus(quotationExists.getStatus());
            }
            quotation.setDeleted(false);

            DOQuotation quotationUpdated = this.quotationRepository.save(quotation);
            return quotationUpdated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public boolean finalizeQuotation(String quotationId) throws CustomException {
        try {

            quotationId = InputValidatorUtil.validateStringProperty("Quotation Id", quotationId);
            if (!this.quotationRepository.isExistsById(quotationId)) {
                throw new DoesNotExistException("Quotation does not exists. Quotation Id : " + quotationId);
            }

            DOQuotation quotation = this.quotationRepository.getItemsById(quotationId);
            
            if (!this.projectRepository.checkProjectAlive(quotation.getProjectId())) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + quotation.getProjectId());
            }

            List<DOQuotation> quotations = this.quotationRepository.getItemsByProjectId(quotation.getProjectId());
            for (DOQuotation quotation1 : quotations) {
                if (!quotation.getId().equals(quotation1.getId())) {
                    this.quotationRepository.setStatus(DataUtil.QUOTATION_STATE_DISCARD, quotation1.getId());
                }
            }

            DOProject project = projectRepository.getById(quotation.getProjectId());
            project.setSystemPrice(quotation.getSystemPrice());
            project.setOutstandingPayment(quotation.getSystemPrice());
            project.setStatus(DataUtil.PROJECT_STATE_APPROVED);
            this.projectRepository.save(project);

            quotation.setStatus(DataUtil.QUOTATION_STATE_FINALIZED);
            quotation.setFinalized(true);
            this.quotationRepository.save(quotation);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
