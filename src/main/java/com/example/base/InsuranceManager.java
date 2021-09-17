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
import com.example.entity.DOInsurance;
import com.example.repository.ProjectRepository;
import com.example.repository.InsuranceRepository;
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
public class InsuranceManager {

    @Autowired
    private InsuranceRepository insuranceRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;

    public InsuranceManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOInsurance> listInsurances(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "insurance");

            return this.insuranceRepository.listInsurances(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countInsurances(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "insurance");
            Object o = this.insuranceRepository.countInsurances(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOInsurance getInsuranceById(String insuranceId) throws CustomException {
        try {

            insuranceId = InputValidatorUtil.validateStringProperty("Insurance Id", insuranceId);
            if (!this.insuranceRepository.isExistsById(insuranceId)) {
                throw new DoesNotExistException("Insurance does not exists. Insurance Id : " + insuranceId);
            }
            return this.insuranceRepository.findById(insuranceId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOInsurance createInsurance(DOInsurance insurance) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", insurance.getProjectId());
            insurance.setProjectId(projectId);

            long applyDate = insurance.getApplyDate();
            if (applyDate <= 0) {
                throw new InvalidInputException("Invalid Apply Date. Apply date : " + applyDate);
            }
            insurance.setApplyDate(applyDate);

            int amountPerAnum = insurance.getAmountPerAnum();
            if (amountPerAnum <= 0) {
                throw new InvalidInputException("Invalid Amount Per Anum. Amount Per Anum : " + amountPerAnum);
            }
            insurance.setAmountPerAnum(amountPerAnum);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            String id = UUID.randomUUID().toString();

            insurance.setId(id);
            insurance.setStatus(DataUtil.STATE_NEW);
            insurance.setDeleted(false);

            DOInsurance insuranceCreated = this.insuranceRepository.save(insurance);
            return insuranceCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteInsurance(String insuranceId) throws CustomException {
        try {

            insuranceId = InputValidatorUtil.validateStringProperty("Insurance Id", insuranceId);
            if (!this.insuranceRepository.isExistsById(insuranceId)) {
                throw new DoesNotExistException("Insurance does not exists.Insurance Id : " + insuranceId);
            }
            this.insuranceRepository.deleteInsurance(true, insuranceId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOInsurance updateInsurance(DOInsurance insurance) throws CustomException {
        try {
            String insuranceId = InputValidatorUtil.validateStringProperty("Insurance Id", insurance.getId());
            insurance.setId(insuranceId);

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", insurance.getProjectId());
            insurance.setProjectId(projectId);

            long applyDate = insurance.getApplyDate();
            if (applyDate <= 0) {
                throw new InvalidInputException("Invalid Apply Date. Apply date : " + applyDate);
            }
            insurance.setApplyDate(applyDate);

            int amountPerAnum = insurance.getAmountPerAnum();
            if (amountPerAnum <= 0) {
                throw new InvalidInputException("Invalid Amount Per Anum. Amount Per Anum : " + amountPerAnum);
            }
            insurance.setAmountPerAnum(amountPerAnum);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.insuranceRepository.isExistsById(insuranceId)) {
                throw new DoesNotExistException("Insurance does not exists.Insurance Id : " + insuranceId);
            }

            insurance.setStatus(DataUtil.QUOTATION_STATE_NEW);
            insurance.setDeleted(false);

            DOInsurance insuranceCreated = this.insuranceRepository.save(insurance);
            return insuranceCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
