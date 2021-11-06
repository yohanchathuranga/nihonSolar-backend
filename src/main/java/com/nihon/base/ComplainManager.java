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
import com.nihon.entity.DOComplain;
import com.nihon.entity.DOStatusCheck;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.ComplainRepository;
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
import yohan.exceptions.InvalidInputException;

/**
 *
 * @author yohan
 */
@Service
public class ComplainManager {

    @Autowired
    private ComplainRepository complainRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private StatusCheckRepository statusCheckRepository;
    private final DAODataUtil dataUtil;

    public ComplainManager(ProjectRepository projectRepository, UserRepository userRepository, StatusCheckRepository statusCheckRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.statusCheckRepository = statusCheckRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOComplain> listComplains(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.isDistinct(), listRequest.getPage(), listRequest.getLimit(), "complain");

            return this.complainRepository.listComplains(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countComplains(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), countRequest.isDistinct(), "complain");
            Object o = this.complainRepository.countComplains(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOComplain getComplainById(String complainId) throws CustomException {
        try {

            complainId = InputValidatorUtil.validateStringProperty("Complain Id", complainId);
            if (!this.complainRepository.isExistsById(complainId)) {
                throw new DoesNotExistException("Complain does not exists. Complain Id : " + complainId);
            }
            return this.complainRepository.findById(complainId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOComplain createComplain(DOComplain complain) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", complain.getProjectId());
            complain.setProjectId(projectId);

            long currentTime = DateTimeUtil.getCurrentTime();
            complain.setDate(currentTime);

            String type = InputValidatorUtil.validateStringProperty("Type", complain.getType());
            complain.setType(type);

            String complian = InputValidatorUtil.validateStringProperty("Complain", complain.getComplain());
            complain.setComplain(complian);

            String id = UUID.randomUUID().toString();

            long solveDate = complain.getSolvedDate();
            if (solveDate > 0) {
                if (solveDate > currentTime) {
                    DOStatusCheck statusCheck = new DOStatusCheck();
                    statusCheck.setProjectId(projectId);
                    statusCheck.setType(DataUtil.STATUS_CHECK_TYPE_COMPLAIN);
                    statusCheck.setStatus(DataUtil.STATUS_CHECK_STATE_NEW);
                    statusCheck.setDeleted(false);
                    statusCheck.setId(id);
                    statusCheck.setCheckNo(statusCheckRepository.getMaxCheckNoByType(projectId, DataUtil.STATUS_CHECK_TYPE_COMPLAIN) + 1);
                    statusCheck.setActualDate(solveDate);
                    statusCheckRepository.save(statusCheck);
                }
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
//            if (!this.projectRepository.checkProjectAlive(projectId)) {
//                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
//            }

            complain.setId(id);
            complain.setStatus(DataUtil.STATE_NEW);
            complain.setDeleted(false);

            DOComplain complainCreated = this.complainRepository.save(complain);
            return complainCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteComplain(String complainId) throws CustomException {
        try {

            complainId = InputValidatorUtil.validateStringProperty("Complain Id", complainId);
            if (!this.complainRepository.isExistsById(complainId)) {
                throw new DoesNotExistException("Complain does not exists.Complain Id : " + complainId);
            }
            this.complainRepository.deleteComplain(true, complainId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOComplain updateComplain(DOComplain complain) throws CustomException {
        try {
            String complainId = InputValidatorUtil.validateStringProperty("Complain Id", complain.getId());
            complain.setId(complainId);

            if (!this.complainRepository.isExistsById(complainId)) {
                throw new DoesNotExistException("Complain does not exists. Complain Id : " + complainId);
            }

            DOComplain complainExists = complainRepository.findById(complainId).get();

            String projectId = complainExists.getProjectId();

            String type = InputValidatorUtil.validateStringProperty("Type", complain.getType());
            complain.setType(type);

            String complian = InputValidatorUtil.validateStringProperty("Complain", complain.getComplain());
            complain.setComplain(complian);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            long solveDate = complain.getSolvedDate();
            if (solveDate > 0 && solveDate != complainExists.getSolvedDate()) {
                if (solveDate > DateTimeUtil.getCurrentTime()) {
                    DOStatusCheck statusCheck = new DOStatusCheck();
                    if (statusCheckRepository.isExistsById(complainId)) {
                        statusCheck = statusCheckRepository.findById(complainId).get();
                        statusCheck.setActualDate(solveDate);
                    } else {
                        statusCheck.setProjectId(projectId);
                        statusCheck.setType(DataUtil.STATUS_CHECK_TYPE_COMPLAIN);
                        statusCheck.setStatus(DataUtil.STATUS_CHECK_STATE_NEW);
                        statusCheck.setDeleted(false);
                        statusCheck.setId(complainId);
                        statusCheck.setCheckNo(statusCheckRepository.getMaxCheckNoByType(projectId, DataUtil.STATUS_CHECK_TYPE_COMPLAIN) + 1);
                        statusCheck.setActualDate(solveDate);
                    }
                    statusCheckRepository.save(statusCheck);

                }
            }

//            if (!this.projectRepository.checkProjectAlive(projectId)) {
//                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
//            }
            complain.setStatus(complainExists.getStatus());
            complain.setProjectId(projectId);
            complain.setDeleted(false);

            DOComplain complainCreated = this.complainRepository.save(complain);
            return complainCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOComplain resolveComplain(String complainId) throws CustomException {
        try {
            complainId = InputValidatorUtil.validateStringProperty("Complain Id", complainId);

            if (!this.complainRepository.isExistsById(complainId)) {
                throw new DoesNotExistException("Complain does not exists. Complain Id : " + complainId);
            }

            this.complainRepository.setStatus(DataUtil.COMPLAIN_RESOLVED, complainId);
            DOComplain complainExists = complainRepository.findById(complainId).get();
            return complainExists;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
