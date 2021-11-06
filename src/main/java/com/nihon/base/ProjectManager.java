/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.base;

import com.nihon.dataaccess.DAODataUtil;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOCustomerFeedback;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOProject;
import com.nihon.entity.DOProjectDetails;
import com.nihon.entity.DOProjectElectricityBoard;
import com.nihon.repository.BankLoanRepository;
import com.nihon.repository.ClearanceRepository;
import com.nihon.repository.ComplainRepository;
import com.nihon.repository.CustomerFeedbackRepository;
import com.nihon.repository.InsuranceRepository;
import com.nihon.repository.PaymentRepository;
import com.nihon.repository.ProjectElectricityBoardRepository;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.QuotationRepository;
import com.nihon.repository.ServiceRepository;
import com.nihon.repository.SiteVisitRepository;
import com.nihon.repository.StatusCheckRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.DateTimeUtil;
import com.nihon.util.IdGenerator;
import com.nihon.util.InputValidatorUtil;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ProjectElectricityBoardManager projectElectricityBoardManager;

    public ProjectManager(UserRepository userRepository, BankLoanRepository bankLoanRepository, ClearanceRepository clearanceRepository, ComplainRepository complainRepository, CustomerFeedbackRepository customerFeedbackRepository, InsuranceRepository insuranceRepository, PaymentRepository paymentRepository, ProjectElectricityBoardRepository projectElectricityBoardRepository, QuotationRepository quotationRepository, ServiceRepository serviceRepository, SiteVisitRepository siteVisitRepository, StatusCheckRepository statusCheckRepository, DAODataUtil dataUtil, IdGenerator idGenerator, ProjectElectricityBoardManager projectElectricityBoardManager) {
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
        this.projectElectricityBoardManager = projectElectricityBoardManager;
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

    @Transactional
    public DOProject createProject(DOProject project) throws CustomException {
        try {
            String userId = InputValidatorUtil.validateStringProperty("User Id ", project.getUserId());
            project.setUserId(userId);

            String maketingOfficer = InputValidatorUtil.validateStringProperty("Maketing Officer", project.getMaketingOfficer());
            project.setMaketingOfficer(maketingOfficer);

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            if (project.getCreatedDate() <= 0) {
                long currentDate = DateTimeUtil.getCurrentTime();
                project.setCreatedDate(currentDate);
            }

            String id = idGenerator.generateId("project", project.getCreatedDate());

            project.setId(id);
            project.setStatus(DataUtil.PROJECT_STATE_NEW);
            project.setDeleted(false);

            DOProject projectCreated = this.projectRepository.save(project);

            DOProjectElectricityBoard projectElectricityBoard = new DOProjectElectricityBoard();
            projectElectricityBoard.setProjectId(id);
            projectElectricityBoardManager.createProjectElectricityBoard(projectElectricityBoard);
            return projectCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public boolean deleteProject(String projectId) throws CustomException {
        try {

            projectId = InputValidatorUtil.validateStringProperty("Project Id", projectId);
            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists.Project Id : " + projectId);
            }
            DOProject projectExists = this.projectRepository.getById(projectId);
            userRepository.deleteUser(true, projectExists.getUserId());
            bankLoanRepository.deleteBankLoanByProjectId(true, projectId);
            clearanceRepository.deleteByProjectId(true, projectId);
            complainRepository.deleteByProjectId(true, projectId);
            customerFeedbackRepository.deleteByProjectId(true, projectId);
            insuranceRepository.deleteByProjectId(true, projectId);
            paymentRepository.deleteByProjectId(true, projectId);
            projectElectricityBoardRepository.deleteByProjectId(true, projectId);
            quotationRepository.deleteByProjectId(true, projectId);
            serviceRepository.deleteByProjectId(true, projectId);
            siteVisitRepository.deleteByProjectId(true, projectId);
            statusCheckRepository.deleteByProjectId(true, projectId);
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

            DOProject projectExists = this.projectRepository.getById(projectId);
            project.setCreatedDate(projectExists.getCreatedDate());
            project.setStatus(projectExists.getStatus());
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
            projectDetails.setClearance(clearanceRepository.getItemsByProjectId(projectId));
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

    @Transactional
    public DOProject cancel(String projectId) throws CustomException {
        try {

            projectId = InputValidatorUtil.validateStringProperty("Project Id", projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            DOProject project = projectRepository.findById(projectId).get();

//            bankLoanRepository.deleteBankLoanByProjectId(true, projectId);
//            clearanceRepository.deleteByProjectId(true, projectId);
//            complainRepository.deleteByProjectId(true, projectId);
//            customerFeedbackRepository.deleteByProjectId(true, projectId);
//            insuranceRepository.deleteByProjectId(true, projectId);
//            paymentRepository.deleteByProjectId(true, projectId);
//            projectElectricityBoardRepository.deleteByProjectId(true, projectId);
//            quotationRepository.deleteByProjectId(true, projectId);
//            serviceRepository.deleteByProjectId(true, projectId);
//            siteVisitRepository.deleteByProjectId(true, projectId);
//            statusCheckRepository.deleteByProjectId(true, projectId);
//            this.projectRepository.deleteProject(true, projectId);
            project.setStatus(DataUtil.PROJECT_STATE_CANCELLED);

            DOProject projectCancelled = this.projectRepository.save(project);
            return projectCancelled;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOProject complete(String projectId) throws CustomException {
        try {

            projectId = InputValidatorUtil.validateStringProperty("Project Id", projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            DOProject project = projectRepository.findById(projectId).get();
            project.setStatus(DataUtil.PROJECT_STATE_COMPLETE);

            DOProject projectCancelled = this.projectRepository.save(project);
            return projectCancelled;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOProject approve(String projectId) throws CustomException {
        try {

            projectId = InputValidatorUtil.validateStringProperty("Project Id", projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            List<DOCustomerFeedback> customerFeedbacks = customerFeedbackRepository.getItemsByProjectId(projectId);
            for (DOCustomerFeedback customerFeedback : customerFeedbacks) {
                if (customerFeedback.getStatus().equals(DataUtil.FEEDBACK_STATE_NEW)) {
                    customerFeedbackRepository.setStatus(DataUtil.STATE_DISCARD, customerFeedback.getId());
                }
            }
            DOProject project = projectRepository.findById(projectId).get();
            return project;
        } catch (CustomException ex) {
            throw ex;
        }

    }
}
