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
import com.example.entity.DOStatusCheck;
import com.example.repository.ProjectRepository;
import com.example.repository.StatusCheckRepository;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
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
public class StatusCheckManager {

    @Autowired
    private StatusCheckRepository statusCheckRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;

    public StatusCheckManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOStatusCheck> listStatusChecks(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "status_check");

            return this.statusCheckRepository.listStatusChecks(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countStatusChecks(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "status_check");
            Object o = this.statusCheckRepository.countStatusChecks(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOStatusCheck getStatusCheckById(String statusCheckId) throws CustomException {
        try {

            statusCheckId = InputValidatorUtil.validateStringProperty("Status Check Id", statusCheckId);
            if (!this.statusCheckRepository.isExistsById(statusCheckId)) {
                throw new DoesNotExistException("Status Check does not exists. Status Check Id : " + statusCheckId);
            }
            return this.statusCheckRepository.findById(statusCheckId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOStatusCheck createStatusCheck(DOStatusCheck statusCheck) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", statusCheck.getProjectId());
            statusCheck.setProjectId(projectId);

            String type = InputValidatorUtil.validateStringProperty("Type", statusCheck.getType());
            statusCheck.setType(type);

            int checkNo = statusCheck.getCheckNo();
            if (checkNo <= 0) {
                throw new InvalidInputException("Invalid Ckeck No. Check No:" + checkNo);
            }
            statusCheck.setCheckNo(checkNo);
            
            long actualDate = statusCheck.getActualDate();
            if (actualDate <= 0) {
                throw new InvalidInputException("Invalid Actual Date. Actual Date:" + actualDate);
            }
            statusCheck.setActualDate(actualDate);


            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            String id = UUID.randomUUID().toString();

            statusCheck.setId(id);
            statusCheck.setStatus(DataUtil.STATUS_CHECK_STATE_NEW);
            statusCheck.setDeleted(false);

            DOStatusCheck statusCheckCreated = this.statusCheckRepository.save(statusCheck);
            return statusCheckCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteStatusCheck(String statusCheckId) throws CustomException {
        try {

            statusCheckId = InputValidatorUtil.validateStringProperty("Status Check Id", statusCheckId);
            if (!this.statusCheckRepository.isExistsById(statusCheckId)) {
                throw new DoesNotExistException("Status Check does not exists.Status Check Id : " + statusCheckId);
            }
            this.statusCheckRepository.deleteStatusCheck(true, statusCheckId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOStatusCheck updateStatusCheck(DOStatusCheck statusCheck) throws CustomException {
        try {

            String statusCheckId = InputValidatorUtil.validateStringProperty("Status Check Id", statusCheck.getId());
            statusCheck.setId(statusCheckId);

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", statusCheck.getProjectId());
            statusCheck.setProjectId(projectId);

            String type = InputValidatorUtil.validateStringProperty("Type", statusCheck.getType());
            statusCheck.setType(type);

            int checkNo = statusCheck.getCheckNo();
            if (checkNo <= 0) {
                throw new InvalidInputException("Invalid Ckeck No. Check No:" + checkNo);
            }
            statusCheck.setCheckNo(checkNo);
            
            long actualDate = statusCheck.getActualDate();
            if (actualDate <= 0) {
                throw new InvalidInputException("Invalid Actual Date. Actual Date:" + actualDate);
            }
            statusCheck.setActualDate(actualDate);


            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.statusCheckRepository.isExistsById(statusCheckId)) {
                throw new DoesNotExistException("Bank loan does not exists. Bank loan Id : " + statusCheckId);
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }


//            statusCheck.setStatus(DataUtil.QUOTATION_STATE_NEW);
            statusCheck.setDeleted(false);

            DOStatusCheck statusCheckCreated = this.statusCheckRepository.save(statusCheck);
            return statusCheckCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
