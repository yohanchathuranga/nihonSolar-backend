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
import com.nihon.entity.DOService;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.ServiceRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.DateTimeUtil;
import com.nihon.util.InputValidatorUtil;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yohan
 */
@Service
public class ServiceManager {
    
    @Autowired
    private ServiceRepository serviceRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;
    
    public ServiceManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.dataUtil = dataUtil;
    }
    
    public List<DOService> listServices(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "service");
            
            return this.serviceRepository.listServices(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }
    
    public DOListCountResult countServices(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "service");
            Object o = this.serviceRepository.countServices(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }
    
    public DOService getServiceById(String serviceId) throws CustomException {
        try {
            
            serviceId = InputValidatorUtil.validateStringProperty("Service Id", serviceId);
            if (!this.serviceRepository.isExistsById(serviceId)) {
                throw new DoesNotExistException("Service does not exists. Service Id : " + serviceId);
            }
            return this.serviceRepository.findById(serviceId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public DOService createService(DOService service) throws CustomException {
        try {
            
            String projectId = InputValidatorUtil.validateStringProperty("Project Id", service.getProjectId());
            service.setProjectId(projectId);
            
            long date = service.getDate();
            if (date <= 0) {
                throw new InvalidInputException("Invalid date. Date : "+date);
            }
            service.setDate(date);
            
            int serviceNo = service.getServiceNo();
            if (serviceNo <= 0) {
                throw new InvalidInputException("Invalid Service no. Service No : "+serviceNo);
            }
            service.setServiceNo(serviceNo);
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            
            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }
            
            String id = UUID.randomUUID().toString();
            
            service.setId(id);
            service.setStatus(DataUtil.STATE_NEW);
            service.setDeleted(false);
            
            DOService serviceCreated = this.serviceRepository.save(service);
            return serviceCreated;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public boolean deleteService(String serviceId) throws CustomException {
        try {
            
            serviceId = InputValidatorUtil.validateStringProperty("Service Id", serviceId);
            if (!this.serviceRepository.isExistsById(serviceId)) {
                throw new DoesNotExistException("Service does not exists.Service Id : " + serviceId);
            }
            this.serviceRepository.deleteService(true, serviceId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public DOService updateService(DOService service) throws CustomException {
        try {
            String serviceId = InputValidatorUtil.validateStringProperty("Service Id", service.getId());
            service.setId(serviceId);
            
            DOService serviceExists = serviceRepository.findById(serviceId).get();
            
            String projectId = serviceExists.getProjectId();
            
            long date = service.getDate();
            if (date <= 0) {
                throw new InvalidInputException("Invalid date. Date : "+date);
            }
            service.setDate(date);
            
            int serviceNo = service.getServiceNo();
            if (serviceNo <= 0) {
                throw new InvalidInputException("Invalid Service no. Service No : "+serviceNo);
            }
            service.setServiceNo(serviceNo);
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            
            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }
            
            if (!this.serviceRepository.isExistsById(serviceId)) {
                throw new DoesNotExistException("Service does not exists.Service Id : " + serviceId);
            }
            
//            service.setStatus(DataUtil.QUOTATION_STATE_NEW);
            service.setProjectId(projectId);
            service.setDeleted(false);
            
            DOService serviceCreated = this.serviceRepository.save(service);
            return serviceCreated;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
}
