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
import com.nihon.entity.DOMailSender;
import com.nihon.entity.DONotification;
import com.nihon.entity.DOSiteVisit;
import com.nihon.entity.DOStatusCheck;
import com.nihon.entity.DOUser;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.SiteVisitRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.DateTimeUtil;
import com.nihon.util.InputValidatorUtil;
import java.util.ArrayList;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yohan.exceptions.AlreadyExistException;

/**
 *
 * @author yohan
 */
@Service
public class SiteVisitManager {

    @Autowired
    private SiteVisitRepository siteVisitRepository;
    private ProjectRepository projectRepository;
    private NotificationManager notificationManager;
    private final DAODataUtil dataUtil;

    public SiteVisitManager(ProjectRepository projectRepository, NotificationManager notificationManager, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.notificationManager = notificationManager;
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

    @Transactional
    public DOSiteVisit createSiteVisit(DOSiteVisit siteVisit) throws CustomException {
        try {

            String projectId = InputValidatorUtil.validateStringProperty("Project Id", siteVisit.getProjectId());
            siteVisit.setProjectId(projectId);

            if (siteVisit.getVisitedDate() <= 0) {
                long currentTime = DateTimeUtil.getCurrentTime();
                siteVisit.setVisitedDate(currentTime);
            }

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

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            if (siteVisitRepository.getItemsByProjectId(projectId) != null) {
                throw new AlreadyExistException("Already exists for Projrct id . Project Id :" + projectId);
            }

            String id = UUID.randomUUID().toString();

            siteVisit.setId(id);
            siteVisit.setStatus(DataUtil.STATE_NEW);
            siteVisit.setDeleted(false);

            DOSiteVisit siteVisitCreated = this.siteVisitRepository.save(siteVisit);
            notificationManager.notificationSent(projectId);
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

            if (!this.siteVisitRepository.isExistsById(siteVisitId)) {
                throw new DoesNotExistException("SiteVisit does not exists.SiteVisit Id : " + siteVisitId);
            }

            DOSiteVisit siteVisitExists = siteVisitRepository.findById(siteVisitId).get();

            String projectId = siteVisitExists.getProjectId();

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

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            siteVisit.setProjectId(projectId);
            siteVisit.setStatus(siteVisitExists.getStatus());
            siteVisit.setDeleted(false);

            DOSiteVisit siteVisitCreated = this.siteVisitRepository.save(siteVisit);
            return siteVisitCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
