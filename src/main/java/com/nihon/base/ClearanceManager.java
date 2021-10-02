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
import com.nihon.entity.DOClearance;
import com.nihon.entity.DOClearanceCollectedDate;
import com.nihon.entity.DOStatusCheck;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.ClearanceRepository;
import com.nihon.repository.StatusCheckRepository;
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
import yohan.exceptions.AlreadyExistException;
import yohan.exceptions.InvalidInputException;

/**
 *
 * @author yohan
 */
@Service
public class ClearanceManager {

    @Autowired
    private ClearanceRepository clearanceRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private StatusCheckRepository statusCheckRepository;
    private final DAODataUtil dataUtil;

    public ClearanceManager(ProjectRepository projectRepository, UserRepository userRepository, StatusCheckRepository statusCheckRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.statusCheckRepository = statusCheckRepository;
        this.dataUtil = dataUtil;
    }

    
    public List<DOClearance> listClearances(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "clearance");

            return this.clearanceRepository.listClearances(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countClearances(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "clearance");
            Object o = this.clearanceRepository.countClearances(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOClearance getClearanceById(String clearanceId) throws CustomException {
        try {

            clearanceId = InputValidatorUtil.validateStringProperty("Clearance Id", clearanceId);
            if (!this.clearanceRepository.isExistsById(clearanceId)) {
                throw new DoesNotExistException("Clearance does not exists. Clearance Id : " + clearanceId);
            }
            return this.clearanceRepository.findById(clearanceId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public DOClearance createClearance(DOClearance clearance) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", clearance.getProjectId());
            clearance.setProjectId(projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            
            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            if (clearanceRepository.getItemsByProjectId(projectId) != null) {
                throw new AlreadyExistException("Already exists for Projrct id . Project Id :" + projectId);
            }
            
            if (clearance.getSubmitDate() <= 0) {
                clearance.setSubmitDate(DateTimeUtil.getCurrentTime());
            }
            long nextWeek = clearance.getSubmitDate();
            DOStatusCheck statusCheck = new DOStatusCheck();
            statusCheck.setProjectId(projectId);
            statusCheck.setType(DataUtil.STATUS_CHECK_TYPE_CLEARANCE);
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

            clearance.setId(id);
            clearance.setStatus(DataUtil.QUOTATION_STATE_NEW);
            clearance.setDeleted(false);

            DOClearance clearanceCreated = this.clearanceRepository.save(clearance);
            return clearanceCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteClearance(String clearanceId) throws CustomException {
        try {

            clearanceId = InputValidatorUtil.validateStringProperty("Clearance Id", clearanceId);
            if (!this.clearanceRepository.isExistsById(clearanceId)) {
                throw new DoesNotExistException("Clearance does not exists.Clearance Id : " + clearanceId);
            }
            this.clearanceRepository.deleteClearance(true, clearanceId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOClearance updateClearance(DOClearance clearance) throws CustomException {
        try {

            String clearanceId = InputValidatorUtil.validateStringProperty("Clearance Id", clearance.getId());
            clearance.setId(clearanceId); 

            DOClearance clearanceExists = clearanceRepository.findById(clearanceId).get();
            String projectId = clearanceExists.getProjectId();
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.clearanceRepository.isExistsById(clearanceId)) {
                throw new DoesNotExistException("Clearance does not exists.Clearance Id : " + clearanceId);
            }
            
            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            clearance.setProjectId(projectId);
            clearance.setStatus(DataUtil.QUOTATION_STATE_NEW);
            clearance.setDeleted(false);

            DOClearance clearanceCreated = this.clearanceRepository.save(clearance);
            return clearanceCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }
    
    @Transactional
    public DOClearance setCollecteDate(DOClearanceCollectedDate clearanceCollectedDate) throws CustomException {
        try {

            String clearanceId = InputValidatorUtil.validateStringProperty("Clearance Id", clearanceCollectedDate.getClearanceId());
            clearanceCollectedDate.setClearanceId(clearanceId);

            long collectedDate = clearanceCollectedDate.getCollectedDate();
            if(collectedDate<=0){
                throw new InvalidInputException("invalid collected date");
            }

            if (!this.clearanceRepository.isExistsById(clearanceId)) {
                throw new DoesNotExistException("Clearance does not exists.Clearance Id : " + clearanceId);
            }
            DOClearance clearance = clearanceRepository.findById(clearanceId).get();
            
            if (!this.projectRepository.checkProjectAlive(clearance.getProjectId())) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + clearance.getProjectId());
            }
            
            List<DOStatusCheck> statusChecks = statusCheckRepository.getItemsByProjectIdAndType(clearance.getProjectId(), DataUtil.STATUS_CHECK_TYPE_CLEARANCE);
            for (DOStatusCheck statusCheck : statusChecks) {
                if(statusCheck.getStatus().equals(DataUtil.STATUS_CHECK_STATE_NEW)){
                    statusCheckRepository.setStatus(DataUtil.STATE_DISCARD, statusCheck.getId());
                }
            }
            clearance.setCollectDate(collectedDate);
            DOClearance clearanceUpdated = this.clearanceRepository.save(clearance);
            return clearanceUpdated;
        } catch (CustomException ex) {
            throw ex;
        }

    }


}
