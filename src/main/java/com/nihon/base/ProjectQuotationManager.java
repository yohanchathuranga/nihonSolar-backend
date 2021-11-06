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
import com.nihon.entity.DOProjectQuotation;
import com.nihon.repository.CustomerFeedbackRepository;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.ProjectQuotationRepository;
import com.nihon.repository.UserRepository;
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
public class ProjectQuotationManager {

    @Autowired
    private ProjectQuotationRepository projectQuotationRepository;
    private ProjectRepository projectRepository;

    private final DAODataUtil dataUtil;

    public ProjectQuotationManager(ProjectRepository projectRepository,DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOProjectQuotation> listProjectQuotations(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.isDistinct(), listRequest.getPage(), listRequest.getLimit(), "project_quotation");

            return this.projectQuotationRepository.listProjectQuotations(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countProjectQuotations(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), countRequest.isDistinct(), "project_quotation");
            Object o = this.projectQuotationRepository.countProjectQuotations(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOProjectQuotation getProjectQuotationById(String projectQuotationId) throws CustomException {
        try {

            projectQuotationId = InputValidatorUtil.validateStringProperty("Project Quotation Id", projectQuotationId);
            if (!this.projectQuotationRepository.isExistsById(projectQuotationId)) {
                throw new DoesNotExistException("Project Quotation does not exists. Project Quotation Id : " + projectQuotationId);
            }
            return this.projectQuotationRepository.findById(projectQuotationId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public DOProjectQuotation createProjectQuotation(DOProjectQuotation projectQuotation) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", projectQuotation.getProjectId());
            projectQuotation.setProjectId(projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }
            String id = UUID.randomUUID().toString();

            projectQuotation.setId(id);
            projectQuotation.setDeleted(false);

            DOProjectQuotation projectQuotationCreated = this.projectQuotationRepository.save(projectQuotation);
            return projectQuotationCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteProjectQuotation(String projectQuotationId) throws CustomException {
        try {

            projectQuotationId = InputValidatorUtil.validateStringProperty("Project Quotation Id", projectQuotationId);
            if (!this.projectQuotationRepository.isExistsById(projectQuotationId)) {
                throw new DoesNotExistException("Project Quotation does not exists.Project Quotation Id : " + projectQuotationId);
            }
            this.projectQuotationRepository.deleteProjectQuotation(true, projectQuotationId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOProjectQuotation updateProjectQuotation(DOProjectQuotation projectQuotation) throws CustomException {
        try {
            String projectQuotationId = InputValidatorUtil.validateStringProperty("ProjectQuotation Id", projectQuotation.getId());
            projectQuotation.setId(projectQuotationId);

            if (!this.projectQuotationRepository.isExistsById(projectQuotationId)) {
                throw new DoesNotExistException("ProjectQuotation does not exists.ProjectQuotation Id : " + projectQuotationId);
            }

            DOProjectQuotation projectQuotationExists = projectQuotationRepository.getItemsById(projectQuotationId);

            String projectId = projectQuotationExists.getProjectId();

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            projectQuotation.setProjectId(projectId);
            projectQuotation.setDeleted(false);

            DOProjectQuotation projectQuotationUpdated = this.projectQuotationRepository.save(projectQuotation);
            return projectQuotationUpdated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    @Transactional
    public boolean selectProjectQuotation(String projectQuotationId) throws CustomException {
        try {

            projectQuotationId = InputValidatorUtil.validateStringProperty("Project Quotation Id", projectQuotationId);
            if (!this.projectQuotationRepository.isExistsById(projectQuotationId)) {
                throw new DoesNotExistException("Project Quotation does not exists. Project Quotation Id : " + projectQuotationId);
            }

            DOProjectQuotation projectQuotation = this.projectQuotationRepository.getItemsById(projectQuotationId);

            if (!this.projectRepository.checkProjectAlive(projectQuotation.getProjectId())) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectQuotation.getProjectId());
            }

            List<DOProjectQuotation> projectQuotations = this.projectQuotationRepository.getItemsByProjectId(projectQuotation.getProjectId());
            for (DOProjectQuotation projectQuotation1 : projectQuotations) {
                if (projectQuotation.getId().equals(projectQuotation1.getId())) {
                    this.projectQuotationRepository.setSelectQuotation(true, projectQuotation1.getId());
                } else {
                    this.projectQuotationRepository.setSelectQuotation(false, projectQuotation1.getId());
                }
            }
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
