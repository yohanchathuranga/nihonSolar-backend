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
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.ComplainRepository;
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
    private final DAODataUtil dataUtil;
    
    public ComplainManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "complain");
            
            return this.complainRepository.listComplains(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }
    
    public DOListCountResult countComplains(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "complain");
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
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }
            
            String id = UUID.randomUUID().toString();
            
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
            
            DOComplain complainExists = complainRepository.findById(complainId).get();
            
            String projectId = complainExists.getProjectId();
            
            String type = InputValidatorUtil.validateStringProperty("Type", complain.getType());
            complain.setType(type);
            
            String complian = InputValidatorUtil.validateStringProperty("Complain", complain.getComplain());
            complain.setComplain(complian);
            
            if (!this.complainRepository.isExistsById(complainId)) {
                throw new DoesNotExistException("Complain does not exists. Complain Id : " + complainId);
            }
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            
            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

//            complain.setStatus(DataUtil.STATE_NEW);
            complain.setProjectId(projectId);
            complain.setDeleted(false);
            
            DOComplain complainCreated = this.complainRepository.save(complain);
            return complainCreated;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
}
