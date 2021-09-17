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
import com.example.entity.DOSiteVisit;
import com.example.repository.ProjectRepository;
import com.example.repository.SiteVisitRepository;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
import com.example.util.DateTimeUtil;
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
public class SiteVisitManager {

    @Autowired
    private SiteVisitRepository siteVisitRepository;
    private ProjectRepository projectRepository;
    private final DAODataUtil dataUtil;

    public SiteVisitManager(ProjectRepository projectRepository, UserRepository userRepository, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.dataUtil = dataUtil;
    }

    public List<DOSiteVisit> listSiteVisits(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "site_visit");

            return this.siteVisitRepository.listSiteVisits(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countSiteVisits(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "site_visit");
            Object o = this.siteVisitRepository.countSiteVisits(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOSiteVisit getSiteVisitById(String siteVisitId) throws CustomException {
        try {

            siteVisitId = InputValidatorUtil.validateStringProperty("Site Visit Id", siteVisitId);
            if (!this.siteVisitRepository.isExistsById(siteVisitId)) {
                throw new DoesNotExistException("Site Visit does not exists. Site Visit Id : " + siteVisitId);
            }
            return this.siteVisitRepository.findById(siteVisitId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOSiteVisit createSiteVisit(DOSiteVisit siteVisit) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", siteVisit.getProjectId());
            siteVisit.setProjectId(projectId);

            long currentTime = DateTimeUtil.getCurrentTime();
            siteVisit.setVisitedDate(currentTime);

            String visitBy = InputValidatorUtil.validateStringProperty("Visit By", siteVisit.getVisitBy());
            siteVisit.setVisitBy(visitBy);

            String roofType = InputValidatorUtil.validateStringProperty("Roof Type", siteVisit.getRoofType());
            siteVisit.setRoofType(roofType);

            if (siteVisit.getNoOfStories() < 0) {
                throw new InvalidInputException("Invalid No of Stories");
            }

            String remarks = InputValidatorUtil.validateStringProperty("Remarks", siteVisit.getRemarks());
            siteVisit.setRemarks(remarks);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            String id = UUID.randomUUID().toString();

            siteVisit.setId(id);
            siteVisit.setStatus(DataUtil.QUOTATION_STATE_NEW);
            siteVisit.setDeleted(false);

            DOSiteVisit siteVisitCreated = this.siteVisitRepository.save(siteVisit);
            return siteVisitCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteSiteVisit(String siteVisitId) throws CustomException {
        try {

            siteVisitId = InputValidatorUtil.validateStringProperty("Site Visit Id", siteVisitId);
            if (!this.siteVisitRepository.isExistsById(siteVisitId)) {
                throw new DoesNotExistException("Site Visit does not exists.Site Visit Id : " + siteVisitId);
            }
            this.siteVisitRepository.deleteSiteVisit(true, siteVisitId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOSiteVisit updateSiteVisit(DOSiteVisit siteVisit) throws CustomException {
        try {
            String siteVisitId = InputValidatorUtil.validateStringProperty("Site Visit Id", siteVisit.getId());
            siteVisit.setId(siteVisitId);

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", siteVisit.getProjectId());
            siteVisit.setProjectId(projectId);

            String visitBy = InputValidatorUtil.validateStringProperty("Visit By", siteVisit.getVisitBy());
            siteVisit.setVisitBy(visitBy);

            String roofType = InputValidatorUtil.validateStringProperty("Roof Type", siteVisit.getRoofType());
            siteVisit.setRoofType(roofType);

            if (siteVisit.getNoOfStories() < 0) {
                throw new InvalidInputException("Invalid No of Stories");
            }

            String remarks = InputValidatorUtil.validateStringProperty("Remarks", siteVisit.getRemarks());
            siteVisit.setRemarks(remarks);

            if (!this.siteVisitRepository.isExistsById(siteVisitId)) {
                throw new DoesNotExistException("SiteVisit does not exists.SiteVisit Id : " + siteVisitId);
            }

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            siteVisit.setStatus(DataUtil.QUOTATION_STATE_NEW);
            siteVisit.setDeleted(false);

            DOSiteVisit siteVisitCreated = this.siteVisitRepository.save(siteVisit);
            return siteVisitCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
