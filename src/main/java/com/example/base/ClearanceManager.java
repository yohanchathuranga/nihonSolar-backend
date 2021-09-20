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
import com.example.entity.DOClearance;
import com.example.repository.ProjectRepository;
import com.example.repository.ClearanceRepository;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
import com.example.util.InputValidatorUtil;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.DoesNotExistException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final DAODataUtil dataUtil;

    public ClearanceManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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

    public DOClearance createClearance(DOClearance clearance) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", clearance.getProjectId());
            clearance.setProjectId(projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
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

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", clearance.getProjectId());
            clearance.setProjectId(projectId);

            String clearanceId = InputValidatorUtil.validateStringProperty("Clearance Id", clearance.getId());            
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            
            if (!this.clearanceRepository.isExistsById(clearanceId)) {
                throw new DoesNotExistException("Clearance does not exists.Clearance Id : " + clearanceId);
            }

            clearance.setStatus(DataUtil.QUOTATION_STATE_NEW);
            clearance.setDeleted(false);

            DOClearance clearanceCreated = this.clearanceRepository.save(clearance);
            return clearanceCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
