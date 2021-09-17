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
import com.example.entity.DOQuotation;
import com.example.repository.ProjectRepository;
import com.example.repository.QuotationRepository;
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
public class QuotationManager {

    @Autowired
    private QuotationRepository quotationRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;

    public QuotationManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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

            if (!this.quotationRepository.isExistsById(quotationId)) {
                throw new DoesNotExistException("Quotation does not exists.Quotation Id : " + quotationId);
            }
            
            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            quotation.setStatus(DataUtil.QUOTATION_STATE_NEW);
            quotation.setDeleted(false);

            DOQuotation quotationCreated = this.quotationRepository.save(quotation);
            return quotationCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
