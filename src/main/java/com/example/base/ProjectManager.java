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
import com.example.entity.DOProject;
import com.example.entity.DOProjectDetails;
import com.example.repository.BankLoanRepository;
import com.example.repository.ClearanceRepository;
import com.example.repository.ComplainRepository;
import com.example.repository.CustomerFeedbackRepository;
import com.example.repository.InsuranceRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.ProjectElectricityBoardRepository;
import com.example.repository.ProjectRepository;
import com.example.repository.QuotationRepository;
import com.example.repository.ServiceRepository;
import com.example.repository.SiteVisitRepository;
import com.example.repository.StatusCheckRepository;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
import com.example.util.DateTimeUtil;
import com.example.util.IdGenerator;
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
public class ProjectManager {
    
    @Autowired
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private BankLoanRepository bankLoanRepository;
    private ClearanceRepository clearanceRepository;
    private ComplainRepository complainRepository;
    private CustomerFeedbackRepository customerFeedbackRepository;
    private InsuranceRepository insuranceRepository;
    private PaymentRepository paymentRepository;
    private ProjectElectricityBoardRepository projectElectricityBoardRepository;
    private QuotationRepository quotationRepository;
    private ServiceRepository serviceRepository;
    private SiteVisitRepository siteVisitRepository;
    private StatusCheckRepository statusCheckRepository;
    private final DAODataUtil dataUtil;
    private IdGenerator idGenerator;
    
    public ProjectManager(UserRepository userRepository, BankLoanRepository bankLoanRepository, ClearanceRepository clearanceRepository, ComplainRepository complainRepository, CustomerFeedbackRepository customerFeedbackRepository, InsuranceRepository insuranceRepository, PaymentRepository paymentRepository, ProjectElectricityBoardRepository projectElectricityBoardRepository, QuotationRepository quotationRepository, ServiceRepository serviceRepository, SiteVisitRepository siteVisitRepository, StatusCheckRepository statusCheckRepository, DAODataUtil dataUtil, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.bankLoanRepository = bankLoanRepository;
        this.clearanceRepository = clearanceRepository;
        this.complainRepository = complainRepository;
        this.customerFeedbackRepository = customerFeedbackRepository;
        this.insuranceRepository = insuranceRepository;
        this.paymentRepository = paymentRepository;
        this.projectElectricityBoardRepository = projectElectricityBoardRepository;
        this.quotationRepository = quotationRepository;
        this.serviceRepository = serviceRepository;
        this.siteVisitRepository = siteVisitRepository;
        this.statusCheckRepository = statusCheckRepository;
        this.dataUtil = dataUtil;
        this.idGenerator = idGenerator;
    }
    
    public List<DOProject> listProjects(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "project");
            
            return this.projectRepository.listProjects(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }
    
    public DOListCountResult countProjects(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "project");
            Object o = this.projectRepository.countProjects(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }
    
    public DOProject getProjectById(String projectId) throws CustomException {
        try {
            
            projectId = InputValidatorUtil.validateStringProperty("Project Id", projectId);
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            return this.projectRepository.findById(projectId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public DOProject createProject(DOProject project) throws CustomException {
        try {
            String userId = InputValidatorUtil.validateStringProperty("User Id ", project.getUserId());
            project.setUserId(userId);
            
            String maketingOfficer = InputValidatorUtil.validateStringProperty("Maketing Officer", project.getMaketingOfficer());
            project.setMaketingOfficer(maketingOfficer);
            
            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }
//            String contactNo = InputValidatorUtil.validateStringProperty("Contact No", project.getContactNo());
//            project.setContact_no(contactNo);
//
//            String email = InputValidatorUtil.validateStringProperty("Email", project.getEmail());
//            project.setEmail(email);
//
//            String nic = InputValidatorUtil.validateStringProperty("NIC", project.getNic());
//            project.setNic(nic);
//
//            String address = InputValidatorUtil.validateStringProperty("Address", project.getAddress());
//            project.setStatus(address);
//
//            String type = InputValidatorUtil.validateStringProperty("Project Type", project.getType());
//            project.setType(type);
//
//            String password = InputValidatorUtil.validateStringProperty("Password", project.getPassword());
//            project.setPassword(password);
//
//            if (!type.toUpperCase().equals(DataUtil.USER_TYPE_CUSTOMER) && !type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
//                throw new InvalidInputException("Invalid type. Type :" + type);
//            }
//            String occupation = InputValidatorUtil.validateStringProperty("Occupation", project.getOccupation());
//            project.setOccupation(occupation);

            String id = idGenerator.generateId("project", DateTimeUtil.getCurrentTime());
            
            project.setId(id);
            project.setStatus(DataUtil.PROJECT_STATE_NEW);
            project.setDeleted(false);
            
            DOProject projectCreated = this.projectRepository.save(project);
            return projectCreated;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public boolean deleteProject(String projectId) throws CustomException {
        try {
            
            projectId = InputValidatorUtil.validateStringProperty("Project Id", projectId);
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists.Project Id : " + projectId);
            }
            this.projectRepository.deleteProject(true, projectId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public DOProject updateProject(DOProject project) throws CustomException {
        try {
            
            String projectId = InputValidatorUtil.validateStringProperty("Project Id", project.getId());
            project.setId(projectId);
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
//
//            String lastName = InputValidatorUtil.validateStringProperty("Last Name", project.getLastName());
//            project.setLastName(lastName);
//
//            String contactNo = InputValidatorUtil.validateStringProperty("Contact No", project.getContactNo());
//            project.setContact_no(contactNo);
//
//            String email = InputValidatorUtil.validateStringProperty("Email", project.getEmail());
//            project.setEmail(email);
//
//            String nic = InputValidatorUtil.validateStringProperty("NIC", project.getNic());
//            project.setNic(nic);
//
//            String address = InputValidatorUtil.validateStringProperty("Address", project.getAddress());
//            project.setStatus(address);
//
//            String type = InputValidatorUtil.validateStringProperty("Project Type", project.getType());
//            project.setType(type);
//
//            String password = InputValidatorUtil.validateStringProperty("Password", project.getPassword());
//            project.setPassword(password);
//
//            if (!type.toUpperCase().equals(DataUtil.USER_TYPE_CUSTOMER) && !type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
//                throw new InvalidInputException("Invalid type. Type :" + type);
//            }
//            String occupation = InputValidatorUtil.validateStringProperty("Occupation", project.getOccupation());
//            project.setOccupation(occupation);

            project.setStatus(DataUtil.PROJECT_STATE_NEW);
            project.setDeleted(false);
            
            DOProject projectCreated = this.projectRepository.save(project);
            return projectCreated;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public DOProjectDetails getProjectDetailsById(String projectId) throws CustomException {
        try {
            DOProjectDetails projectDetails = new DOProjectDetails();
            
            projectId = InputValidatorUtil.validateStringProperty("Project Id", projectId);
            
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }
            DOProject project = projectRepository.getById(projectId);
            projectDetails.setProject(project);
            projectDetails.setCustomer(userRepository.getById(project.getUserId()));
            projectDetails.setBankLoan(bankLoanRepository.getItemsByProjectId(projectId));
            projectDetails.setClearances(clearanceRepository.getItemsByProjectId(projectId));
            projectDetails.setComplains(complainRepository.getItemsByProjectId(projectId));
            projectDetails.setCustomerFeedbacks(customerFeedbackRepository.getItemsByProjectId(projectId));
            projectDetails.setInsurance(insuranceRepository.getItemsByProjectId(projectId));
            projectDetails.setPayments(paymentRepository.getItemsByProjectId(projectId));
            projectDetails.setProjectElectricityBoard(projectElectricityBoardRepository.getItemsByProjectId(projectId));
            projectDetails.setQuotations(quotationRepository.getItemsByProjectId(projectId));
            projectDetails.setServices(serviceRepository.getItemsByProjectId(projectId));
            projectDetails.setSiteVisit(siteVisitRepository.getItemsByProjectId(projectId));
            projectDetails.setStatusChecks(statusCheckRepository.getItemsByProjectId(projectId));
            
            return projectDetails;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
}
