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
import com.nihon.entity.DOStatusCheck;
import com.nihon.entity.DOStatusChecked;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.StatusCheckRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.InputValidatorUtil;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yohan.exceptions.AlreadyExistException;

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
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.isDistinct(), listRequest.getPage(), listRequest.getLimit(), "status_check");

            return this.statusCheckRepository.listStatusChecks(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countStatusChecks(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), countRequest.isDistinct(), "status_check");
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

            if (!type.equals(DataUtil.STATUS_CHECK_TYPE_BANK_LOAN) && !type.equals(DataUtil.STATUS_CHECK_TYPE_CLEARANCE) && !type.equals(DataUtil.STATUS_CHECK_TYPE_INSURANCE) && !type.equals(DataUtil.STATUS_CHECK_TYPE_SITE_VISIT) && !type.equals(DataUtil.STATUS_CHECK_TYPE_QUOTATION) && !type.equals(DataUtil.STATUS_CHECK_TYPE_SERVICE) && !type.equals(DataUtil.STATUS_CHECK_TYPE_COMPLAIN)) {
                throw new InvalidInputException("Invalid type");
            }

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

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            if (statusCheckRepository.isExistsByProjectIdTypeAndWeekNo(projectId, type, checkNo)) {
                throw new AlreadyExistException("Already exists");
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

            DOStatusCheck statusCheckExists = statusCheckRepository.findById(statusCheckId).get();

            String projectId = statusCheckExists.getProjectId();

//            String type = InputValidatorUtil.validateStringProperty("Type", statusCheck.getType());
//            statusCheck.setType(type);
//            
//            if(!type.equals(DataUtil.STATUS_CHECK_TYPE_BANK_LOAN) && !type.equals(DataUtil.STATUS_CHECK_TYPE_CLEARANCE)){
//                throw new InvalidInputException("Invalid type");
//            }
//
//            int checkNo = statusCheck.getCheckNo();
//            if (checkNo <= 0) {
//                throw new InvalidInputException("Invalid Ckeck No. Check No:" + checkNo);
//            }
//            statusCheck.setCheckNo(checkNo);
//            
//            long actualDate = statusCheck.getActualDate();
//            if (actualDate <= 0) {
//                throw new InvalidInputException("Invalid Actual Date. Actual Date:" + actualDate);
//            }
//            statusCheck.setActualDate(actualDate);
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.statusCheckRepository.isExistsById(statusCheckId)) {
                throw new DoesNotExistException("Bank loan does not exists. Bank loan Id : " + statusCheckId);
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            statusCheck.setType(statusCheckExists.getType());
            statusCheck.setActualDate(statusCheckExists.getActualDate());
            statusCheck.setCheckNo(statusCheckExists.getCheckNo());;
            statusCheck.setProjectId(projectId);
            statusCheck.setStatus(statusCheckExists.getStatus());
            statusCheck.setDeleted(false);

            DOStatusCheck statusCheckCreated = this.statusCheckRepository.save(statusCheck);
            return statusCheckCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOStatusCheck setStatusChecked(DOStatusChecked statusChecked) throws CustomException {
        try {

            String statusCheckId = InputValidatorUtil.validateStringProperty("Status Check Id", statusChecked.getStatusCheckId());
            statusChecked.setStatusCheckId(statusCheckId);

            if (!this.statusCheckRepository.isExistsById(statusCheckId)) {
                throw new DoesNotExistException("Bank loan does not exists. Bank loan Id : " + statusCheckId);
            }

            long checkedDate = statusChecked.getCheckedDate();
            if (checkedDate <= 0) {
                throw new InvalidInputException("Invalid Checked Date.");
            }
            statusChecked.setCheckedDate(checkedDate);
            DOStatusCheck statusCheck = statusCheckRepository.findById(statusCheckId).get();

            statusCheck.setCheckedDate(checkedDate);
            statusCheck.setStatus(DataUtil.STATUS_CHECK_STATE_CHECKED);

            DOStatusCheck statusCheckCreated = this.statusCheckRepository.save(statusCheck);
            return statusCheckCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }
}
